import 'package:drivero_automa/config/app_config.dart';
import 'package:drivero_automa/core/widgets/background_page.dart';
import 'package:drivero_automa/gen/assets.gen.dart';
import 'package:drivero_automa/routes/app_router.dart';
import 'package:drivero_automa/theme/text_theme.dart';
import 'package:drivero_automa/theme/theme.dart';
import 'package:flutter/material.dart';
import 'package:go_router/go_router.dart';

class ProfileScreen extends StatefulWidget {
  const ProfileScreen({super.key});

  @override
  State<ProfileScreen> createState() => _ProfileScreenState();
}

class _ProfileScreenState extends State<ProfileScreen> {
  @override
  Widget build(BuildContext context) {
    return SafeArea(
      child: Scaffold(
        body: BackgroundPageWidget(
          child: Stack(
            children: [
              // Body putih rounded di bawah header
              Container(
                margin: const EdgeInsets.only(top: 180),
                padding: EdgeInsets.symmetric(
                  horizontal: AppSetting.deviceWidth * 0.03,
                ),
                width: double.infinity,
                decoration: const BoxDecoration(
                  color: Colors.white,
                  borderRadius: BorderRadius.only(
                    topLeft: Radius.circular(50),
                    topRight: Radius.circular(50),
                  ),
                ),
                child: Column(
                  crossAxisAlignment: CrossAxisAlignment.start,
                  children: [
                    const SizedBox(height: 60),
                    Padding(
                      padding: const EdgeInsets.symmetric(horizontal: 20),
                      child: Text(
                        "Account Setting",
                        style: AppTextTheme.bodyText1,
                      ),
                    ),
                    const SizedBox(height: 10),
                    ListTile(
                      leading: Icon(
                        Icons.build,
                        color: MyTheme.color.secondary,
                      ),
                      title: Text(
                        "Maintenance Information",
                        style: AppTextTheme.bodyText1.copyWith(
                          color: MyTheme.color.primary,
                        ),
                      ),
                      trailing: const Icon(Icons.arrow_forward_ios, size: 16),
                      onTap: () {},
                    ),
                    ListTile(
                      leading: Icon(
                        Icons.assignment,
                        color: MyTheme.color.secondary,
                      ),
                      title: Text(
                        "Asurance Information",
                        style: AppTextTheme.bodyText1.copyWith(
                          color: MyTheme.color.primary,
                        ),
                      ),
                      trailing: const Icon(Icons.arrow_forward_ios, size: 16),
                      onTap: () {},
                    ),
                    ListTile(
                      leading: Icon(
                        Icons.settings,
                        color: MyTheme.color.secondary,
                      ),
                      title: Text(
                        "Settings",
                        style: AppTextTheme.bodyText1.copyWith(
                          color: MyTheme.color.primary,
                        ),
                      ),
                      trailing: const Icon(Icons.arrow_forward_ios, size: 16),
                      onTap: () {},
                    ),
                    Padding(
                      padding: const EdgeInsets.symmetric(vertical: 20),
                      child: SizedBox(
                        width: double.infinity,
                        height: 50,
                        child: InkWell(
                          borderRadius: BorderRadius.circular(25),
                          onTap: () {},
                          child: Container(
                            decoration: BoxDecoration(
                              gradient: MyTheme
                                  .color
                                  .darkLightBlueGradientLeftToRight, // LinearGradient
                              borderRadius: BorderRadius.circular(25),
                            ),
                            child: Row(
                              mainAxisAlignment: MainAxisAlignment.center,
                              children: const [
                                Icon(Icons.logout, color: Colors.white),
                                SizedBox(width: 8),
                                Text(
                                  "Log Out",
                                  style: TextStyle(color: Colors.white),
                                ),
                              ],
                            ),
                          ),
                        ),
                      ),
                    ),
                  ],
                ),
              ),

              // Profile info (foto, nama, email, tombol edit) → ditaruh di atas rounded
              Positioned(
                top: 70,
                left: 20,
                right: 20,
                child: Row(
                  children: [
                    CircleAvatar(
                      radius: 40,
                      backgroundImage: AssetImage(Assets.images.ppUser.path),
                    ),
                    SizedBox(width: AppSetting.setWidth(20)),
                    Column(
                      crossAxisAlignment: CrossAxisAlignment.start,
                      children: const [
                        Text(
                          "Beji Driver",
                          style: TextStyle(
                            color: Colors.white,
                            fontSize: 18,
                            fontWeight: FontWeight.bold,
                          ),
                        ),
                        Text(
                          "bejidrivers@gmail.com",
                          style: TextStyle(color: Colors.white70),
                        ),
                      ],
                    ),
                    const Spacer(),
                    ElevatedButton(
                      style: ElevatedButton.styleFrom(
                        backgroundColor: Colors.white,
                        foregroundColor: Colors.blue,
                        shape: RoundedRectangleBorder(
                          borderRadius: BorderRadius.circular(20),
                        ),
                      ),
                      onPressed: () {
                        context.pushNamed(AppRouter.editProfile);
                      },
                      child: Text(
                        "Edit",
                        style: AppTextTheme.bodyText1.copyWith(
                          color: MyTheme.color.primary,
                          fontWeight: FontWeight.bold,
                        ),
                      ),
                    ),
                  ],
                ),
              ),

              // Judul "My Profile"
              const Positioned(
                top: 20,
                left: 20,
                child: Text(
                  "My Profile",
                  style: TextStyle(
                    color: Colors.white,
                    fontSize: 20,
                    fontWeight: FontWeight.bold,
                  ),
                ),
              ),
            ],
          ),
        ),
      ),
    );
  }
}
