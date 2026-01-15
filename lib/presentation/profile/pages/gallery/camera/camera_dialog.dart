import 'dart:io';
import 'package:flutter/material.dart';
import 'package:camera/camera.dart';
import 'package:drivero_automa/theme/theme.dart';
import 'package:drivero_automa/config/app_config.dart';

class CameraDialog {
  static Future<void> showSaveDialog(
    BuildContext context,
    XFile image, {
    // 1. PERBAIKAN: Ubah jadi Function(String) agar bisa kirim nama
    required Function(String name) onSaved, 
    required VoidCallback onCancel,
  }) async {
    final nameController = TextEditingController();

    return showDialog(
      context: context,
      barrierDismissible: false,
      builder: (BuildContext dialogContext) {
        return Center(
          child: Container(
            width: AppSetting.deviceWidth * 0.9,
            margin: EdgeInsets.symmetric(
              horizontal: AppSetting.setWidth(10),
            ),
            child: Dialog(
              insetPadding: EdgeInsets.zero,
              shape: RoundedRectangleBorder(
                borderRadius: BorderRadius.circular(AppSetting.setWidth(16)),
              ),
              backgroundColor: MyTheme.color.white,
              child: Padding(
                padding: EdgeInsets.all(AppSetting.setWidth(20)),
                child: Column(
                  mainAxisSize: MainAxisSize.min,
                  children: [
                    // Header
                    Row(
                      mainAxisAlignment: MainAxisAlignment.spaceBetween,
                      children: [
                        Text(
                          "Simpan Foto",
                          style: TextStyle(
                            fontSize: AppSetting.setFontSize(20),
                            fontWeight: FontWeight.bold,
                            color: MyTheme.color.primary,
                          ),
                        ),
                        IconButton(
                          icon: Icon(
                            Icons.close,
                            color: MyTheme.color.danger,
                            size: AppSetting.setFontSize(22),
                          ),
                          onPressed: () {
                            Navigator.of(dialogContext).pop();
                            onCancel();
                          },
                        ),
                      ],
                    ),
                    SizedBox(height: AppSetting.setHeight(16)),

                    // Preview Gambar Kecil (Opsional - Agar user tahu apa yang disimpan)
                    ClipRRect(
                      borderRadius: BorderRadius.circular(8),
                      child: Image.file(
                        File(image.path),
                        height: AppSetting.setHeight(150),
                        width: double.infinity,
                        fit: BoxFit.cover,
                      ),
                    ),
                    SizedBox(height: AppSetting.setHeight(16)),

                    // Input nama
                    TextField(
                      controller: nameController,
                      style: TextStyle(
                        color: MyTheme.color.black,
                        fontSize: AppSetting.setFontSize(14),
                      ),
                      decoration: InputDecoration(
                        labelText: "Nama File (Opsional)",
                        hintText: "Contoh: Bagian Depan",
                        labelStyle: TextStyle(color: MyTheme.color.primary),
                        contentPadding: EdgeInsets.symmetric(
                          horizontal: AppSetting.setWidth(12),
                          vertical: AppSetting.setHeight(12),
                        ),
                        enabledBorder: OutlineInputBorder(
                          borderRadius:
                              BorderRadius.circular(AppSetting.setWidth(12)),
                          borderSide: BorderSide(color: MyTheme.color.primary),
                        ),
                        focusedBorder: OutlineInputBorder(
                          borderRadius:
                              BorderRadius.circular(AppSetting.setWidth(12)),
                          borderSide: BorderSide(color: MyTheme.color.primary),
                        ),
                      ),
                    ),

                    SizedBox(height: AppSetting.setHeight(24)),

                    // Tombol Simpan
                    SizedBox(
                      width: double.infinity,
                      child: ElevatedButton(
                        style: ElevatedButton.styleFrom(
                          backgroundColor: MyTheme.color.primary,
                          padding: EdgeInsets.symmetric(
                            vertical: AppSetting.setHeight(12),
                          ),
                          shape: RoundedRectangleBorder(
                            borderRadius:
                                BorderRadius.circular(AppSetting.setWidth(10)),
                          ),
                        ),
                        onPressed: () {
                          // 2. PERBAIKAN: Hanya kirim data, jangan simpan file di sini
                          // Biarkan BLoC yang bekerja
                          
                          Navigator.of(dialogContext).pop(); // Tutup dialog
                          
                          // Kirim teks inputan user ke parent (CameraPage -> CameraBloc)
                          onSaved(nameController.text.trim()); 
                        },
                        child: Text(
                          "Simpan",
                          style: TextStyle(
                            fontSize: AppSetting.setFontSize(16),
                            color: MyTheme.color.white,
                            fontWeight: FontWeight.bold,
                          ),
                        ),
                      ),
                    ),
                  ],
                ),
              ),
            ),
          ),
        );
      },
    );
  }
}