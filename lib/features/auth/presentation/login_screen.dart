import 'package:drivero_automa/features/auth/presentation/bloc/auth_bloc.dart';
import 'package:drivero_automa/features/auth/presentation/bloc/auth_event.dart';
import 'package:drivero_automa/features/auth/presentation/bloc/auth_state.dart';
import 'package:drivero_automa/gen/assets.gen.dart';
import 'package:drivero_automa/gen/fonts.gen.dart';
import 'package:drivero_automa/theme/color_theme.dart';
import 'package:flutter/material.dart';
import 'package:drivero_automa/routes/app_router.dart';
import 'package:drivero_automa/theme/theme.dart';
import 'package:drivero_automa/config/app_config.dart';
import 'package:flutter_bloc/flutter_bloc.dart';
import 'package:go_router/go_router.dart';

class ArcClipper extends CustomClipper<Path> {
  @override
  Path getClip(Size size) {
    Path path = Path();
    path.lineTo(size.width, 0);
    path.lineTo(size.width, size.height - 60);
    path.quadraticBezierTo(size.width / 2, size.height, 0, size.height - 60);
    path.close();
    return path;
  }

  @override
  bool shouldReclip(CustomClipper<Path> oldClipper) => false;
}

class LoginScreen extends StatefulWidget {
  const LoginScreen({super.key});

  @override
  State<LoginScreen> createState() => _LoginScreenState();
}

class _LoginScreenState extends State<LoginScreen> {
  TextEditingController emailController = TextEditingController();
  TextEditingController passwordController = TextEditingController();
  bool _isPasswordVisible = false;

  @override
  Widget build(BuildContext context) {
    final deviceWidth = AppSetting.deviceWidth;
    final deviceHeight = AppSetting.deviceHeight;

    final headerHeight = deviceHeight * 0.35;

    final containerWidth = deviceWidth * 0.25;
    final containerHeight = deviceHeight * 0.13;
    final containerPaddingVertical = deviceHeight * 0.015;
    final containerPaddingHorizontal = deviceWidth * 0.015;
    final logoSize = containerWidth * 0.7;
    final containerTop =
        headerHeight - (containerHeight / 2) - deviceHeight * 0.03;

    return Scaffold(
      backgroundColor: MyTheme.color.white,
      body: BlocListener<AuthBloc, AuthState>(
        listener: (context, state) {
          if (state is Authenticated) {
            context.goNamed(AppRouter.home);
          }
          if (state is AuthFailure) {
            ScaffoldMessenger.of(
              context,
            ).showSnackBar(SnackBar(content: Text(state.message)));
          }
        },
        child: BlocBuilder<AuthBloc, AuthState>(
          builder: (context, state) {
            return Column(
              children: [
                SizedBox(
                  height: headerHeight + containerHeight / 2,
                  child: Stack(
                    children: [
                      // Header biru dengan curve + shadow mengikuti curve
                      Positioned(
                        top: 0,
                        left: 0,
                        right: 0,
                        child: PhysicalShape(
                          clipper: ArcClipper(),
                          color: MyTheme.color.primary,
                          shadowColor: MyTheme.color.black.withOpacity(0.9),
                          elevation: 8,

                          child: SizedBox(
                            height: headerHeight,
                            width: deviceWidth,
                          ),
                        ),
                      ),

                      // Container putih dengan logo
                      Positioned(
                        top: containerTop,
                        left: (deviceWidth - containerWidth) / 2,
                        child: Container(
                          width: containerWidth,
                          height: containerHeight,
                          padding: EdgeInsets.symmetric(
                            horizontal: containerPaddingHorizontal,
                            vertical: containerPaddingVertical,
                          ),
                          decoration: BoxDecoration(
                            color: MyTheme.color.white,
                            borderRadius: BorderRadius.circular(20),
                            boxShadow: [
                              BoxShadow(
                                color: MyTheme.color.black.withOpacity(0.1),
                                spreadRadius: 1,
                                blurRadius: 5,
                                offset: const Offset(0, 3),
                              ),
                            ],
                          ),
                          child: Center(
                            child: Image.asset(
                              Assets.images.logoLogin.path,
                              width: logoSize,
                              height: logoSize,
                              fit: BoxFit.contain,
                            ),
                          ),
                        ),
                      ),
                    ],
                  ),
                ),

                // Form Login
                Expanded(
                  child: SingleChildScrollView(
                    padding: EdgeInsets.fromLTRB(
                      deviceWidth * 0.06,
                      deviceHeight * 0.02,
                      deviceWidth * 0.06,
                      deviceHeight * 0.10, // 👈 tambahan jarak bawah
                    ),
                    child: Column(
                      mainAxisAlignment: MainAxisAlignment.center,
                      crossAxisAlignment: CrossAxisAlignment.center,
                      children: [
                        Text(
                          'Welcome Back!',
                          style: TextStyle(
                            foreground: Paint()
                              ..shader = MyTheme
                                  .color
                                  .lightDarkBlueGradientLeftToRight
                                  .createShader(
                                    Rect.fromLTWH(0.0, 0.0, 200.0, 70.0),
                                  ),
                            fontFamily: FontFamily.inter,
                            fontSize: deviceWidth * 0.06,
                            fontWeight: FontWeight.bold,
                          ),
                          textAlign: TextAlign.center,
                        ),
                        SizedBox(height: deviceHeight * 0.03),

                        // Username Field
                        TextFormField(
                          controller: emailController,
                          decoration: InputDecoration(
                            hintText: 'Username',
                            prefixIcon: Icon(
                              Icons.person_outline,
                              color: Colors.grey[600],
                            ),
                            filled: true,
                            fillColor: Colors.grey[200],
                            border: OutlineInputBorder(
                              borderRadius: BorderRadius.circular(30),
                              borderSide: BorderSide.none,
                            ),
                          ),
                        ),
                        SizedBox(height: deviceHeight * 0.02),

                        // Password Field
                        TextFormField(
                          controller: passwordController,
                          obscureText: !_isPasswordVisible,
                          decoration: InputDecoration(
                            hintText: 'Password',
                            prefixIcon: Icon(
                              Icons.lock_outline,
                              color: Colors.grey[600],
                            ),
                            suffixIcon: IconButton(
                              icon: Icon(
                                !_isPasswordVisible
                                    ? Icons.visibility_off_outlined
                                    : Icons.visibility_outlined,
                                color: MyTheme.color.grey,
                              ),
                              onPressed: () {
                                setState(() {
                                  _isPasswordVisible = !_isPasswordVisible;
                                });
                              },
                            ),
                            filled: true,
                            fillColor: MyTheme.color.grey200,
                            border: OutlineInputBorder(
                              borderRadius: BorderRadius.circular(30),
                              borderSide: BorderSide.none,
                            ),
                          ),
                        ),
                        SizedBox(height: deviceHeight * 0.03),

                        // Login Button
                        state is AuthLoading
                            ? const CircularProgressIndicator()
                            : SizedBox(
                                width: double.infinity,
                                child: ElevatedButton(
                                  onPressed: () {
                                    context.read<AuthBloc>().add(
                                      LoginEvent(
                                        emailController.text,
                                        passwordController.text,
                                      ),
                                    );
                                  },
                                  style: ElevatedButton.styleFrom(
                                    backgroundColor: MyTheme.color.primary,
                                    foregroundColor: MyTheme.color.white,
                                    padding: EdgeInsets.symmetric(
                                      vertical: deviceHeight * 0.02,
                                    ),
                                    shape: RoundedRectangleBorder(
                                      borderRadius: BorderRadius.circular(30),
                                    ),
                                  ),
                                  child: Text(
                                    'Login',
                                    style: TextStyle(
                                      fontSize: deviceWidth * 0.045,
                                      fontWeight: FontWeight.bold,
                                    ),
                                  ),
                                ),
                              ),
                        if (state is AuthFailure)
                          Text(
                            state.message,
                            style: TextStyle(
                              color: Colors.red,
                              fontSize: deviceWidth * 0.035,
                            ),
                          ),
                        SizedBox(height: deviceHeight * 0.015),

                        // Forgot Password
                        TextButton(
                          onPressed: () {},
                          child: Text(
                            'Forgot Password?',
                            style: TextStyle(
                              color: MyTheme.color.primary,
                              fontSize: deviceWidth * 0.035,
                            ),
                          ),
                        ),
                      ],
                    ),
                  ),
                ),
              ],
            );
          },
        ),
      ),
    );
  }
}
