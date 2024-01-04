from django.contrib import admin
from django.urls import path
from core import views as core_views

urlpatterns = [
    path('', core_views.login_page),
    path('admin-page', core_views.admin_page),
    path('user-page/<int:user_id>', core_views.user_page),
    path('admin/', admin.site.urls),
]
