import 'package:drivero_automa/config/app_config.dart';
import 'package:drivero_automa/core/widgets/background_page.dart';
import 'package:drivero_automa/gen/assets.gen.dart';
import 'package:drivero_automa/presentation/auth/bloc/auth_bloc.dart';
import 'package:drivero_automa/presentation/auth/bloc/auth_event.dart';
import 'package:drivero_automa/presentation/auth/bloc/auth_state.dart';
import 'package:drivero_automa/routes/app_router.dart';
import 'package:drivero_automa/theme/text_theme.dart';
import 'package:drivero_automa/theme/theme.dart';
import 'package:flutter/material.dart';
import 'package:flutter_bloc/flutter_bloc.dart';
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
              /// ==============================
              /// BODY PUTIH DENGAN LIST TILE
              /// ==============================
              Container(
                margin: EdgeInsets.only(top: AppSetting.setHeight(180)),
                padding: EdgeInsets.symmetric(
                  horizontal: AppSetting.setWidth(16),
                ),
                width: double.infinity,
                decoration: BoxDecoration(
                  color: MyTheme.color.white,
                  borderRadius: BorderRadius.only(
                    topLeft: Radius.circular(AppSetting.setWidth(50)),
                    topRight: Radius.circular(AppSetting.setWidth(50)),
                  ),
                ),
                child: SingleChildScrollView(
                  physics: const BouncingScrollPhysics(),
                  child: Padding(
                    padding: EdgeInsets.only(top: AppSetting.setHeight(60)),
                    child: Column(
                      crossAxisAlignment: CrossAxisAlignment.start,
                      children: [
                        Padding(
                          padding: EdgeInsets.symmetric(
                            horizontal: AppSetting.setWidth(20),
                          ),
                          child: Text(
                            "Account Setting",
                            style: AppTextTheme.bodyText1,
                          ),
                        ),
                        Space.h(10),
                        _buildListTile(
                          icon: Icons.build,
                          title: "Maintenance Information",
                          onTap: () {},
                        ),
                        _buildListTile(
                          icon: Icons.assignment,
                          title: "Asurance Information",
                          onTap: () {},
                        ),
                        _buildListTile(
                          icon: Icons.settings,
                          title: "Settings",
                          onTap: () {},
                        ),
                        _buildListTile(
                          icon: Icons.photo_library,
                          title: "Gallery",
                          onTap: () {
                            context.push(
                              '${AppRouter.profile}${AppRouter.gallery}',
                            );
                          },
                        ),
                        Padding(
                          padding: EdgeInsets.symmetric(
                            vertical: AppSetting.setHeight(20),
                          ),
                          child: SizedBox(
                            width: double.infinity,
                            height: AppSetting.setHeight(50),
                            child: InkWell(
                              borderRadius: BorderRadius.circular(25),
                              onTap: () {
                                context.read<AuthBloc>().add(LogoutEvent());
                                context.go(AppRouter.login);
                              },
                              child: Container(
                                decoration: BoxDecoration(
                                  gradient: MyTheme
                                      .color
                                      .darkLightBlueGradientLeftToRight,
                                  borderRadius: BorderRadius.circular(25),
                                ),
                                child: Row(
                                  mainAxisAlignment: MainAxisAlignment.center,
                                  children: [
                                    Icon(
                                      Icons.logout,
                                      color: MyTheme.color.white,
                                      size: AppSetting.setWidth(22),
                                    ),
                                    Space.w(8),
                                    Text(
                                      "Log Out",
                                      style: TextStyle(
                                        color: MyTheme.color.white,
                                        fontSize: AppSetting.setFontSize(14),
                                      ),
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
                ),
              ),

              /// ==============================
              /// PROFILE HEADER (FOTO + NAMA)
              /// ==============================
              Positioned(
                top: AppSetting.setHeight(70),
                left: AppSetting.setWidth(20),
                right: AppSetting.setWidth(20),
                child: LayoutBuilder(
                  builder: (context, constraints) {
                    return Row(
                      crossAxisAlignment: CrossAxisAlignment.center,
                      children: [
                        CircleAvatar(
                          radius: AppSetting.setWidth(35),
                          backgroundImage: AssetImage(
                            Assets.images.ppUser.path,
                          ),
                        ),
                        Space.w(15),
                        Expanded(
                          child: BlocBuilder<AuthBloc, AuthState>(
                            builder: (context, state) => Column(
                              crossAxisAlignment: CrossAxisAlignment.start,
                              children: [
                                if (state is Authenticated)
                                  Text(
                                    //oi
                                    state.user.name,
                                    overflow: TextOverflow.ellipsis,
                                    style: TextStyle(
                                      color: MyTheme.color.white,
                                      fontSize: AppSetting.setFontSize(18),
                                      fontWeight: FontWeight.bold,
                                    ),
                                  ),
                                if (state is Authenticated)
                                  Text(
                                    state.user.email,
                                    overflow: TextOverflow.ellipsis,
                                    style: TextStyle(
                                      color: Colors.white70,
                                      fontSize: AppSetting.setFontSize(13),
                                    ),
                                    maxLines: 1,
                                  ),
                              ],
                            ),
                          ),
                        ),
                        Space.w(10),
                        ElevatedButton(
                          style: ElevatedButton.styleFrom(
                            backgroundColor: MyTheme.color.white,
                            foregroundColor: MyTheme.color.primary,
                            padding: EdgeInsets.symmetric(
                              horizontal: AppSetting.setWidth(15),
                              vertical: AppSetting.setHeight(5),
                            ),
                            shape: RoundedRectangleBorder(
                              borderRadius: BorderRadius.circular(20),
                            ),
                          ),
                          onPressed: () {
                            context.pushNamed(AppRouter.editProfile);
                          },
                          child: Text(
                            "Edit",
                            style: TextStyle(
                              color: MyTheme.color.primary,
                              fontWeight: FontWeight.bold,
                              fontSize: AppSetting.setFontSize(12),
                            ),
                          ),
                        ),
                      ],
                    );
                  },
                ),
              ),
              Positioned(
                top: AppSetting.setHeight(20),
                left: AppSetting.setWidth(20),
                child: Text(
                  "My Profile",
                  style: TextStyle(
                    color: MyTheme.color.white,
                    fontSize: AppSetting.setFontSize(20),
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

  /// ==============================
  /// WIDGET LIST TILE REUSABLE
  /// ==============================
  Widget _buildListTile({
    required IconData icon,
    required String title,
    required VoidCallback onTap,
  }) {
    return ListTile(
      leading: Icon(icon, color: MyTheme.color.secondary),
      title: Text(
        title,
        style: AppTextTheme.bodyText1.copyWith(color: MyTheme.color.primary),
      ),
      trailing: const Icon(Icons.arrow_forward_ios, size: 16),
      onTap: onTap,
    );
  }
}
