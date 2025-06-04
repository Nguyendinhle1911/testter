import 'package:flutter/material.dart';
import 'package:flutter_driver/driver_extension.dart'; // Thêm để hỗ trợ Flutter Driver
import 'dang_ky_man_hinh.dart'; // Import file chứa DangKyManHinh

void main() {
  enableFlutterDriverExtension(); // Kích hoạt Flutter Driver
  runApp(MyApp());
}

class MyApp extends StatelessWidget {
  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      title: 'Đăng ký Facebook',
      theme: ThemeData(
        primarySwatch: Colors.blue,
        fontFamily: 'Roboto',
      ),
      home: DangKyManHinh(),
    );
  }
}