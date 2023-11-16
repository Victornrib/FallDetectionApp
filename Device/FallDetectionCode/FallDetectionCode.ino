//-----To set MPU6050-----
//To see how to connect MPU to ESP32, see this link: https://www.electrorules.com/esp32-with-mpu6050-accelerometer-gyroscope-and-temperature-sensor-arduino-ide/

//-----To configure blynk-----
//Download the arduino ESP32 library from "Tools -> Manage Libraries..."
//Then download the Blynk library from "Tools -> Manage Libraries..." or from this link https://github.com/blynkkk/blynk-library
//Put the library folder inside the libraries folder from arduino
//Download the blynk legacy app in your phone and create an account
//Create a project and get the project token which will be sent to your email
//Paste the authentication code inside the char auth[] string in the arduino code
//In your phone, open your project add a notification and email receiver
//Run your project in your phone

//-----To set WiFi-----
//Create a hotspot in your phone with a ssid and a password (Dont use spaces!)
//Paste both of them in the ssid and pass strings in the arduino code

//-----To run-----
//Set the board to ESP32 Dev Module
//Set your COM port (Whatever one is available)
//Set Serial to 115200
//Compile to your ESP32

//-----To monitor-----
//Open your Serial monitor
//Open the 115200 baud
//OBS: If nothing appears, press the reset (EN) button on the arduino

#include <Wire.h>
#define BLYNK_PRINT Serial
#include <WiFi.h>
#include <BlynkSimpleEsp32.h>

//Code for making it work on the ESP8266
//#include <ESP8266WiFi.h>
//#include <BlynkSimpleEsp8266.h>

const int MPU_addr = 0x68; // I2C address of the MPU-6050
int16_t AcX, AcY, AcZ, Tmp, GyX, GyY, GyZ;
float ax = 0, ay = 0, az = 0, gx = 0, gy = 0, gz = 0;

boolean fall = false; //stores if a fall has occurred
boolean trigger1 = false; //stores if first trigger (lower threshold) has occurred
boolean trigger2 = false; //stores if second trigger (upper threshold) has occurred
boolean trigger3 = false; //stores if third trigger (orientation change) has occurred

byte trigger1count = 0; //stores the counts past since trigger 1 was set true
byte trigger2count = 0; //stores the counts past since trigger 2 was set true
byte trigger3count = 0; //stores the counts past since trigger 3 was set true

int angleChange = 0;

// WiFi network info.

//ESP-8266 Victor Token:04lT0Ozxj2yIMh8MuyIphOnVucm8vArB

//ESP-32 Victor Token
char auth[] = "YsSBnNK04as8fl0IVMJIiRERnBUpyIYd"; //Auth code sent via Email
const char *ssid = "testXiaomi"; // Enter your Wi-Fi Name
const char *pass = "Viniri00"; // Enter your Wi-Fi Password

 
void setup() {
  Serial.begin(115200);
  Blynk.begin(auth, ssid, pass);
   
  Wire.begin();
  Wire.beginTransmission(MPU_addr);
  Wire.write(0x6B);  // PWR_MGMT_1 register
  Wire.write(0);     // set to zero (wakes up the MPU-6050)
  Wire.endTransmission(true);
   
  Serial.println("Wrote to IMU");
  Serial.println("Connecting to ");
  Serial.println(ssid);
  WiFi.begin(ssid, pass);
  while (WiFi.status() != WL_CONNECTED)
  {
    delay(500);
    Serial.print(".");              // print … till not connected
  }
  Serial.println("");
  Serial.println("WiFi connected");
}

 
void loop() {
  Blynk.run();
  mpu_read();
  ax = (AcX - 2050) / 16384.00;
  ay = (AcY - 77) / 16384.00;
  az = (AcZ - 1947) / 16384.00;
  gx = (GyX + 270) / 131.07;
  gy = (GyY - 351) / 131.07;
  gz = (GyZ + 136) / 131.07;
   
  // calculating Amplitute vactor for 3 axis
  float Raw_Amp = pow(pow(ax, 2) + pow(ay, 2) + pow(az, 2), 0.5);
  int Amp = Raw_Amp * 10;  // Mulitiplied by 10 bcz values are between 0 to 1
  Serial.println(Amp);
   
  if (Amp <= 2 && trigger2 == false) { //if AM breaks lower threshold (0.4g)     
    trigger1 = true;     
    Serial.println("TRIGGER 1 ACTIVATED");   
  }   
   
  if (trigger1 == true) {     
     trigger1count++;     
     if (Amp >= 12) { //if AM breaks upper threshold (3g)
        trigger2 = true;
        Serial.println("TRIGGER 2 ACTIVATED");
        trigger1 = false; trigger1count = 0;
     }
  }
      
  if (trigger2 == true) {
     trigger2count++;
     angleChange = pow(pow(gx, 2) + pow(gy, 2) + pow(gz, 2), 0.5); Serial.println(angleChange);
       
     if (angleChange >= 30 && angleChange <= 400) { //if orientation changes by between 80-100 degrees       
        trigger3 = true; trigger2 = false; trigger2count = 0;       
        Serial.println(angleChange);       
        Serial.println("TRIGGER 3 ACTIVATED");     
     }   
  }   
      
  if (trigger3 == true) {     
     trigger3count++;     
      
     if (trigger3count >= 10) {
        angleChange = pow(pow(gx, 2) + pow(gy, 2) + pow(gz, 2), 0.5);
        //delay(10);
         
        Serial.println(angleChange);
        if ((angleChange >= 0) && (angleChange <= 10)) { //if orientation changes remains between 0-10 degrees         
           fall = true; trigger3 = false; trigger3count = 0;         
           Serial.println(angleChange);
        }       
        else { //user regained normal orientation         
           trigger3 = false; trigger3count = 0;         
           Serial.println("TRIGGER 3 DEACTIVATED");       
        }     
     }   
  }   
  
  if (fall == true) { //in event of a fall detection     
     Serial.println("FALL DETECTED");     
     Blynk.notify("Alert : Fall Detected…! take action immediately.");     
     Blynk.email("victornrib@gmail.com", "Alert : Fall Detected…!", "Alert : Fall Detected…! take action immediately.");     
     fall = false;   
  }   
   
  if (trigger2count >= 6) { //allow 0.5s for orientation change
     trigger2 = false; trigger2count = 0;
     Serial.println("TRIGGER 2 DECACTIVATED");
  }
  if (trigger1count >= 6) { //allow 0.5s for AM to break upper threshold
     trigger1 = false; trigger1count = 0;
     Serial.println("TRIGGER 1 DECACTIVATED");
  }
  delay(100);
}


void mpu_read() {
  Wire.beginTransmission(MPU_addr);
  Wire.write(0x3B);  // starting with register 0x3B (ACCEL_XOUT_H)
  Wire.endTransmission(false);
  Wire.requestFrom(MPU_addr, 14, true); // request a total of 14 registers
  
  AcX = Wire.read() << 8 | Wire.read(); // 0x3B (ACCEL_XOUT_H) & 0x3C (ACCEL_XOUT_L)
  AcY = Wire.read() << 8 | Wire.read(); // 0x3D (ACCEL_YOUT_H) & 0x3E (ACCEL_YOUT_L)
  AcZ = Wire.read() << 8 | Wire.read(); // 0x3F (ACCEL_ZOUT_H) & 0x40 (ACCEL_ZOUT_L)
  Tmp = Wire.read() << 8 | Wire.read(); // 0x41 (TEMP_OUT_H) & 0x42 (TEMP_OUT_L)
  GyX = Wire.read() << 8 | Wire.read(); // 0x43 (GYRO_XOUT_H) & 0x44 (GYRO_XOUT_L)
  GyY = Wire.read() << 8 | Wire.read(); // 0x45 (GYRO_YOUT_H) & 0x46 (GYRO_YOUT_L)
  GyZ = Wire.read() << 8 | Wire.read(); // 0x47 (GYRO_ZOUT_H) & 0x48 (GYRO_ZOUT_L)
}
