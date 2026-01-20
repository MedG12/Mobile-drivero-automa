import 'dart:async';
import 'package:camera/camera.dart';
import 'package:flutter/material.dart';
import 'package:flutter_bloc/flutter_bloc.dart';
import 'package:intl/intl.dart';

// --- SESUAIKAN IMPORT INI DENGAN FILE KAMU ---
import 'package:drivero_automa/presentation/profile/pages/gallery/camera/LFramePainter.dart';
import 'package:drivero_automa/theme/theme.dart';
import 'package:drivero_automa/config/app_config.dart';

// Import Bloc
import 'bloc/camera_bloc.dart';
import 'bloc/camera_event.dart';
import 'bloc/camera_state.dart';
import 'camera_dialog.dart';

class CameraPage extends StatefulWidget {
  final List<CameraDescription> cameras;

  const CameraPage({
    super.key, 
    required this.cameras, 
  });

  @override
  State<CameraPage> createState() => _CameraPageState();
}

class _CameraPageState extends State<CameraPage> {
  late CameraController _cameraController;
  late Future<void> _initializeControllerFuture;
  
  // Logic Switch Camera
  int _selectedCameraIndex = 0;

  // Timer
  Timer? _timer;
  String _timeString = "";

  // ✅ VARIABLE BARU: Penanda agar lokasi diminta cuma sekali setelah kamera nyala
  bool _isLocationRequested = false; 

  @override
  void initState() {
    super.initState();
    // ✅ 1. KAMERA LANGSUNG DI-INIT DI SINI (Biar cepat muncul)
    _initCamera(_selectedCameraIndex);
    _startTimer();
  }

  // Fungsi Inisialisasi Kamera
  void _initCamera(int cameraIndex) {
    if (widget.cameras.isEmpty) return;

    _cameraController = CameraController(
      widget.cameras[cameraIndex],
      ResolutionPreset.high,
      enableAudio: false,
    );
    _initializeControllerFuture = _cameraController.initialize();
  }

  // Fungsi Ganti Kamera
  void _onSwitchCamera() {
    if (widget.cameras.length < 2) return; 

    setState(() {
      _selectedCameraIndex = (_selectedCameraIndex == 0) ? 1 : 0;
    });

    _initCamera(_selectedCameraIndex);
  }

  void _startTimer() {
    _timer = Timer.periodic(const Duration(seconds: 1), (timer) {
      if (mounted) {
        setState(() {
          _timeString = DateFormat('dd MMM yyyy • HH:mm:ss').format(DateTime.now());
        });
      }
    });
  }

  @override
  void dispose() {
    _cameraController.dispose();
    _timer?.cancel();
    super.dispose();
  }

  @override
  Widget build(BuildContext context) {
    return BlocProvider(
      // ❌ JANGAN panggil InitCameraLocation di sini dulu. Biarkan kosong.
      create: (context) => CameraBloc(), 
      child: BlocListener<CameraBloc, CameraState>(
        listener: (context, state) {
          if (state is CameraSaveSuccess) {
            ScaffoldMessenger.of(context).showSnackBar(
              const SnackBar(
                content: Text("Foto berhasil disimpan & di-watermark!"),
                backgroundColor: Colors.green,
              ),
            );
            Future.delayed(const Duration(milliseconds: 500), () {
              if (context.mounted) Navigator.of(context).pop(true);
            });
          } else if (state is CameraFailure) {
            ScaffoldMessenger.of(context).showSnackBar(
              SnackBar(content: Text(state.message), backgroundColor: Colors.red),
            );
          }
        },
        child: Builder(
          builder: (innerContext) {
            return Scaffold(
              backgroundColor: Colors.black,
              body: Stack(
                fit: StackFit.expand,
                children: [
                  // --- LAYER 1: KAMERA PREVIEW ---
                  FutureBuilder<void>(
                    future: _initializeControllerFuture,
                    builder: (context, snapshot) {
                      if (snapshot.connectionState == ConnectionState.done) {
                        
                        // ✅ 🔥 LOGIC BARU: MINTA LOKASI SETELAH KAMERA READY 🔥
                        if (!_isLocationRequested) {
                          // Tandai biar gak dipanggil berulang-ulang
                          _isLocationRequested = true; 
                          
                          // Panggil Bloc setelah frame kamera selesai dirender
                          WidgetsBinding.instance.addPostFrameCallback((_) {
                            innerContext.read<CameraBloc>().add(InitCameraLocation());
                          });
                        }

                        return CameraPreview(_cameraController);
                      }
                      // Loading saat kamera sedang inisialisasi
                      return const Center(child: CircularProgressIndicator(color: Colors.white));
                    },
                  ),

                  // --- LAYER 2: FRAME GUIDE ---
                  Center(
                    child: CustomPaint(
                      painter: LFramePainter(),
                      child: SizedBox(
                        width: MediaQuery.of(context).size.width * 0.85,
                        height: MediaQuery.of(context).size.height * 0.55,
                      ),
                    ),
                  ),

                  // --- LAYER 3: HEADER (BACK & SWITCH CAMERA) ---
                  Positioned(
                    top: 0,
                    left: 0,
                    right: 0,
                    child: SafeArea(
                      child: Padding(
                        padding: const EdgeInsets.symmetric(horizontal: 16, vertical: 10),
                        child: Row(
                          mainAxisAlignment: MainAxisAlignment.spaceBetween,
                          children: [
                            // Tombol Back
                            CircleAvatar(
                              backgroundColor: Colors.black45,
                              child: IconButton(
                                icon: const Icon(Icons.arrow_back, color: Colors.white),
                                onPressed: () => Navigator.pop(context),
                              ),
                            ),

                            // Tombol Switch Camera
                            if (widget.cameras.length > 1)
                              CircleAvatar(
                                backgroundColor: Colors.black45,
                                child: IconButton(
                                  icon: const Icon(Icons.flip_camera_ios, color: Colors.white),
                                  onPressed: _onSwitchCamera,
                                ),
                              ),
                          ],
                        ),
                      ),
                    ),
                  ),

                  // --- LAYER 4: BOTTOM CONTROL ---
                  Positioned(
                    bottom: 0,
                    left: 0,
                    right: 0,
                    child: Container(
                      padding: const EdgeInsets.only(bottom: 40, top: 30),
                      decoration: const BoxDecoration(
                        gradient: LinearGradient(
                          begin: Alignment.bottomCenter,
                          end: Alignment.topCenter,
                          colors: [Colors.black87, Colors.transparent],
                          stops: [0.2, 1.0],
                        ),
                      ),
                      child: Column(
                        mainAxisSize: MainAxisSize.min,
                        children: [
                          // Info Waktu
                          Text(
                            _timeString,
                            style: const TextStyle(
                                color: Colors.white,
                                fontWeight: FontWeight.bold,
                                fontSize: 14),
                          ),
                          const SizedBox(height: 4),

                          // Info Lokasi (BlocBuilder)
                          BlocBuilder<CameraBloc, CameraState>(
                            buildWhen: (p, c) => c is CameraLocationUpdated,
                            builder: (context, state) {
                              String loc = "Menunggu Kamera..."; 
                              
                              // Ubah teks status sesuai kondisi
                              if (!_isLocationRequested) {
                                loc = "Memuat Kamera...";
                              } else if (state is CameraInitial) {
                                loc = "Mencari lokasi...";
                              } else if (state is CameraLocationUpdated) {
                                loc = state.address;
                              }

                              return Padding(
                                padding: const EdgeInsets.symmetric(horizontal: 30),
                                child: Text(
                                  loc,
                                  textAlign: TextAlign.center,
                                  maxLines: 1,
                                  overflow: TextOverflow.ellipsis,
                                  style: const TextStyle(color: Colors.white70, fontSize: 12),
                                ),
                              );
                            },
                          ),
                          
                          const SizedBox(height: 30),

                          // --- TOMBOL CAPTURE ---
                          InkWell(
                            onTap: () async {
                              try {
                                await _initializeControllerFuture;
                                final image = await _cameraController.takePicture();
                                if (!innerContext.mounted) return;
                                
                                // Tampilkan dialog simpan
                                _showSaveDialog(innerContext, image);
                              } catch (e) {
                                debugPrint("Error capture: $e");
                              }
                            },
                            borderRadius: BorderRadius.circular(50),
                            child: Container(
                              width: 90, 
                              height: 90,
                              decoration: BoxDecoration(
                                shape: BoxShape.circle,
                                color: MyTheme.color.primary,
                                border: Border.all(color: Colors.white, width: 4),
                                boxShadow: [
                                  BoxShadow(
                                    color: MyTheme.color.primary.withOpacity(0.5),
                                    blurRadius: 20,
                                    spreadRadius: 2,
                                  )
                                ],
                              ),
                              child: const Icon(Icons.camera_alt, color: Colors.white, size: 40),
                            ),
                          ),
                        ],
                      ),
                    ),
                  ),

                  // --- LAYER 5: LOADING OVERLAY (Saat Save) ---
                  BlocBuilder<CameraBloc, CameraState>(
                    builder: (context, state) {
                      if (state is CameraSaveLoading) {
                        return Container(
                          color: Colors.black54,
                          child: const Center(
                              child: CircularProgressIndicator(color: Colors.white)),
                        );
                      }
                      return const SizedBox.shrink();
                    },
                  ),
                ],
              ),
            );
          },
        ),
      ),
    );
  }

  void _showSaveDialog(BuildContext context, XFile image) {
    CameraDialog.showSaveDialog(
      context,
      image,
      onSaved: (String customName) {
        // 1. Ambil State Bloc saat ini
        final currentState = context.read<CameraBloc>().state;
        String locationText = "-";

        if (currentState is CameraLocationUpdated) {
          locationText = currentState.address;
        }

        // 2. Kirim Event Save
        context.read<CameraBloc>().add(
              SaveCapturedPhoto(
                image: image, 
                customName: customName,
                location: locationText,
              ),
            );
      },
      onCancel: () {},
    );
  }
}