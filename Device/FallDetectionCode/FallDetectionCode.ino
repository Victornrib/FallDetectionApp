#include <Wire.h>
#include "BluetoothSerial.h"

#if !defined(CONFIG_BT_ENABLED) || !defined(CONFIG_BLUEDROID_ENABLED)
#error Bluetooth is not enabled! Please run `make menuconfig` to and enable it
#endif

#define ONBOARD_LED 16
//Will probably change to 4 when it is the normal LED

BluetoothSerial SerialBT;

int buttonState = 0;
const int buttonPin = 33;     // the number of the pushbutton pin

const int MPU_addr = 0x68; // I2C address of the MPU-6050
//const char FALL[14] = "FALL DETECTED";
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

 
void setup() {
  
  Serial.begin(115200);
  SerialBT.begin("ESP32test");
  Serial.println("The device started, now you can pair it with bluetooth!");

  //Serial.println("Waiting for be paired to start measuring...");

  pinMode(buttonPin, INPUT);

  pinMode(ONBOARD_LED, OUTPUT);
  digitalWrite(ONBOARD_LED, HIGH);
   
  Wire.begin();
  Wire.beginTransmission(MPU_addr);
  Wire.write(0x6B);  // PWR_MGMT_1 register
  Wire.write(0);     // set to zero (wakes up the MPU-6050)
  Wire.endTransmission(true);
}

 
void loop() {

  checkForManualAlert();
  /*
   * Ideally it would only start measure when it receives the signal from the app, but for now it still not works properly
  if (SerialBT.available()) {
    if (SerialBT.read() == "Start") {
      startMeasure();
    }
  }
  */
  startMeasure();
}

void checkForManualAlert() {
  
  //If button is hold for 10 seconds it sends a manual alert to app
  
  int secs = 0;
  while (secs < 10) {
    buttonState = digitalRead(buttonPin);
    if (buttonState == HIGH) {
      return;
    }
    else {
      ledBlink();
      secs += 1;
      Serial.print("Countdown time: ");
      Serial.println(10-secs);
    }
  }
  sendAlert();
}

void startMeasure() {

  //Detects falls and initiates the alert countdown if a fall is detected
  
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
     //Comment next line and uncomment the other after
     initiateAlertCountdown();  
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

void initiateAlertCountdown() {
  //Standard 30 second alert count down for sending message to device
  //Can be deactivated if button is pressed
  int secs = 0;
  while (secs < 30) {
    buttonState = digitalRead(buttonPin);
    if (buttonState == HIGH) {
      Serial.println("Alert canceled.");
      digitalWrite(ONBOARD_LED, HIGH);
      return;
    }
    ledBlink();
    secs += 1;
    Serial.print("Countdown time: ");
    Serial.println(30-secs);
  }
  sendAlert();
}

void ledBlink() {
  //Blinks LED 4 times per second
  digitalWrite(ONBOARD_LED, LOW);   // turn the LED on (HIGH is the voltage level)
  delay(250);                       // wait for a second
  digitalWrite(ONBOARD_LED, HIGH);    // turn the LED off by making the voltage LOW
  delay(250);
  digitalWrite(ONBOARD_LED, LOW);   // turn the LED on (HIGH is the voltage level)
  delay(250);                       // wait for a second
  digitalWrite(ONBOARD_LED, HIGH);    // turn the LED off by making the voltage LOW
  delay(250);
}

void sendAlert() {
  //Send alert to app
  Serial.println("Alert sent");
  SerialBT.write(0);
  waitingState();
}

void waitingState() {
  //Stays in this function with LED on until button is pressed 
  
  digitalWrite(ONBOARD_LED, LOW);
  buttonState = digitalRead(buttonPin);

  //Stays in loop until button is pressed
  while(buttonState == LOW) {
    buttonState = digitalRead(buttonPin);
  }
  
  digitalWrite(ONBOARD_LED, HIGH);
  Serial.println("Reseting");
}

void mpu_read() {
  //Reads values from MPU6050
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