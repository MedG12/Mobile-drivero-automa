import 'dart:io';
import 'package:flutter/material.dart';
import 'package:flutter_bloc/flutter_bloc.dart';
import 'package:flutter_svg/flutter_svg.dart';
import 'package:go_router/go_router.dart';
import 'package:camera/camera.dart';
import 'package:intl/intl.dart';

// --- IMPORT SESUAIKAN DENGAN PROJECT KAMU ---
import 'package:drivero_automa/gen/assets.gen.dart';
import 'package:drivero_automa/theme/theme.dart';
import 'package:drivero_automa/config/app_config.dart';

// Import Bloc
import 'bloc/gallery_bloc.dart';
import 'bloc/gallery_event.dart';
import 'bloc/gallery_state.dart';

class GalleryScreen extends StatefulWidget {
  const GalleryScreen({super.key});

  @override
  State<GalleryScreen> createState() => _GalleryScreenState();
}

class _GalleryScreenState extends State<GalleryScreen> {
  List<CameraDescription> _cameras = [];
  
  // State untuk Filter
  DateTime? _filterDate; 

  @override
  void initState() {
    super.initState();
    _initCameras();
  }

  Future<void> _initCameras() async {
    try {
      final cameras = await availableCameras();
      if (mounted) {
        setState(() {
          _cameras = cameras;
        });
      }
    } catch (e) {
      debugPrint("Gagal ambil kamera: $e");
    }
  }

  // Logic: Membuka Date Picker
  void _showFilterDatePicker() async {
    final DateTime? picked = await showDatePicker(
      context: context,
      initialDate: _filterDate ?? DateTime.now(),
      firstDate: DateTime(2020),
      lastDate: DateTime.now(),
      builder: (context, child) {
        return Theme(
          data: ThemeData.light().copyWith(
            colorScheme: ColorScheme.light(primary: MyTheme.color.primary),
          ),
          child: child!,
        );
      },
    );

    if (picked != null) {
      setState(() {
        _filterDate = picked;
      });
    }
  }

  // Logic: Reset Filter
  void _resetFilter() {
    setState(() {
      _filterDate = null;
    });
  }

  // --- LOGIC BARU: PREVIEW IMAGE ---
  void _showImagePreview(BuildContext context, File imageFile, String imageName) {
    Navigator.of(context).push(
      MaterialPageRoute(
        builder: (context) => Scaffold(
          backgroundColor: Colors.black, 
          appBar: AppBar(
            backgroundColor: Colors.black,
            iconTheme: const IconThemeData(color: Colors.white),
            title: Text(
              imageName,
              style: const TextStyle(color: Colors.white, fontSize: 16),
            ),
          ),
          body: Center(
            // InteractiveViewer membuat gambar bisa di-ZOOM
            child: InteractiveViewer(
              panEnabled: true, 
              boundaryMargin: const EdgeInsets.all(20),
              minScale: 0.5,
              maxScale: 4,
              child: Hero(
                tag: imageFile.path, // Tag harus sama dengan di _buildImageCard
                child: Image.file(
                  imageFile,
                  fit: BoxFit.contain,
                ),
              ),
            ),
          ),
        ),
      ),
    );
  }

  @override
  Widget build(BuildContext context) {
    return BlocProvider(
      create: (context) => GalleryBloc()..add(LoadGalleryImages()),
      child: Scaffold(
        backgroundColor: MyTheme.color.grey200,
        body: Column(
          children: [
            // ===== 1. Custom AppBar dengan Filter =====
            _buildAppBar(context),

            // ===== 2. Body List (Grouped by Date) =====
            Expanded(
              child: Builder(
                builder: (innerContext) {
                  return RefreshIndicator(
                    onRefresh: () async {
                      innerContext.read<GalleryBloc>().add(LoadGalleryImages());
                    },
                    child: BlocBuilder<GalleryBloc, GalleryState>(
                      builder: (context, state) {
                        if (state is GalleryLoading) {
                          return Center(
                            child: CircularProgressIndicator(color: MyTheme.color.primary),
                          );
                        }

                        if (state is GalleryLoaded) {
                          // 1. Ambil File Valid
                          List<File> allImages = state.images.whereType<File>().toList();

                          // 2. Terapkan Filter (Jika user memilih tanggal)
                          if (_filterDate != null) {
                            allImages = allImages.where((file) {
                              try {
                                final lastMod = file.lastModifiedSync();
                                return lastMod.year == _filterDate!.year &&
                                       lastMod.month == _filterDate!.month &&
                                       lastMod.day == _filterDate!.day;
                              } catch (e) {
                                return false;
                              }
                            }).toList();
                          }

                          if (allImages.isEmpty) {
                            return _buildEmptyState();
                          }

                          // 3. Kelompokkan berdasarkan Tanggal (Folder Logic)
                          return _buildGroupedGallery(allImages);
                        }

                        if (state is GalleryError) {
                          return Center(child: Text(state.message, style: const TextStyle(color: Colors.red)));
                        }

                        return _buildEmptyState();
                      },
                    ),
                  );
                },
              ),
            ),
          ],
        ),
      ),
    );
  }

  // --- WIDGET LOGIC ---

  Widget _buildAppBar(BuildContext context) {
    return Container(
      width: double.infinity,
      color: MyTheme.color.white,
      padding: EdgeInsets.only(
        top: AppSetting.setHeight(40),
        left: AppSetting.setWidth(16),
        right: AppSetting.setWidth(16),
        bottom: AppSetting.setHeight(10),
      ),
      child: Row(
        mainAxisAlignment: MainAxisAlignment.spaceBetween,
        children: [
          // KIRI: Back & Title
          Row(
            children: [
              // Tombol Back
              InkWell(
                onTap: () => context.pop(),
                child: Container(
                  decoration: BoxDecoration(
                    color: MyTheme.color.primary,
                    borderRadius: BorderRadius.circular(AppSetting.setWidth(10)),
                  ),
                  padding: EdgeInsets.symmetric(
                    horizontal: AppSetting.setWidth(14),
                    vertical: AppSetting.setHeight(6),
                  ),
                  child: const Icon(Icons.arrow_back, color: Colors.white, size: 20),
                ),
              ),
              
              const SizedBox(width: 12),
              Column(
                crossAxisAlignment: CrossAxisAlignment.start,
                children: [
                  Text(
                    "Dokumentasi",
                    style: TextStyle(
                      fontSize: AppSetting.setFontSize(16),
                      fontWeight: FontWeight.bold,
                      color: MyTheme.color.primary,
                    ),
                  ),
                  // Tampilkan info filter jika aktif
                  if (_filterDate != null)
                    Text(
                      DateFormat("dd MMM yyyy").format(_filterDate!),
                      style: TextStyle(fontSize: 10, color: Colors.grey[600]),
                    ),
                ],
              ),
            ],
          ),

          // KANAN: Filter & Camera
          Row(
            children: [
              // Tombol Filter
              IconButton(
                icon: Icon(
                  _filterDate == null ? Icons.filter_alt_outlined : Icons.filter_alt_off,
                  color: _filterDate == null ? Colors.grey : Colors.red,
                ),
                onPressed: _filterDate == null ? _showFilterDatePicker : _resetFilter,
                tooltip: "Filter Tanggal",
              ),

              // Tombol Kamera
              Builder(
                builder: (ctx) {
                  return GestureDetector(
                    onTap: () async {
                      if (_cameras.isNotEmpty) {
                        final result = await context.pushNamed(
                          'camera', 
                          extra: _cameras, 
                        );

                        if (result == true && ctx.mounted) {
                          ctx.read<GalleryBloc>().add(LoadGalleryImages());
                        }
                      } else {
                        ScaffoldMessenger.of(context).showSnackBar(
                          const SnackBar(content: Text("Kamera tidak ditemukan")),
                        );
                      }
                    },
                    child: Container(
                      padding: const EdgeInsets.all(8),
                      decoration: BoxDecoration(
                        color: MyTheme.color.grey200,
                        shape: BoxShape.circle,
                      ),
                      child: SvgPicture.asset(
                        Assets.icons.addCamera, 
                        width: 24, height: 24,
                      ),
                    ),
                  );
                },
              ),
            ],
          ),
        ],
      ),
    );
  }

  // --- LOGIC GROUPING FOLDER ---

  Widget _buildGroupedGallery(List<File> images) {
    // 1. Sort gambar dari yang terbaru (Descending)
    images.sort((a, b) => b.lastModifiedSync().compareTo(a.lastModifiedSync()));

    // 2. Grouping ke Map<String, List<File>>
    Map<String, List<File>> groupedImages = {};

    for (var image in images) {
      final date = image.lastModifiedSync();
      final now = DateTime.now();
      String key;

      if (date.year == now.year && date.month == now.month && date.day == now.day) {
        key = "Hari Ini";
      } else if (date.year == now.year && date.month == now.month && date.day == now.day - 1) {
        key = "Kemarin";
      } else {
        key = DateFormat("dd MMMM yyyy", "id_ID").format(date); 
      }

      if (groupedImages[key] == null) {
        groupedImages[key] = [];
      }
      groupedImages[key]!.add(image);
    }

    // 3. Render ListView berisi Header + Grid
    return ListView.builder(
      padding: EdgeInsets.only(bottom: AppSetting.setHeight(50)),
      itemCount: groupedImages.keys.length,
      itemBuilder: (context, index) {
        String dateKey = groupedImages.keys.elementAt(index);
        List<File> imagesInGroup = groupedImages[dateKey]!;

        return Column(
          crossAxisAlignment: CrossAxisAlignment.start,
          children: [
            // --- HEADER (FOLDER TITLE) ---
            Padding(
              padding: const EdgeInsets.fromLTRB(16, 20, 16, 10),
              child: Row(
                children: [
                  const Icon(Icons.folder_open, size: 18, color: Colors.grey),
                  const SizedBox(width: 8),
                  Text(
                    dateKey,
                    style: TextStyle(
                      fontWeight: FontWeight.bold,
                      fontSize: AppSetting.setFontSize(14),
                      color: Colors.grey[800],
                    ),
                  ),
                  const Spacer(),
                  Text(
                    "${imagesInGroup.length} Foto",
                    style: TextStyle(fontSize: 12, color: Colors.grey[500]),
                  ),
                ],
              ),
            ),

            // --- GRID GAMBAR ---
            _buildGridForGroup(imagesInGroup),
          ],
        );
      },
    );
  }

  Widget _buildGridForGroup(List<File> images) {
    return GridView.builder(
      shrinkWrap: true, // PENTING: Agar bisa masuk dalam ListView
      physics: const NeverScrollableScrollPhysics(), // Scroll ikut ListView induk
      padding: const EdgeInsets.symmetric(horizontal: 16),
      itemCount: images.length,
      gridDelegate: SliverGridDelegateWithFixedCrossAxisCount(
        crossAxisCount: 2,
        crossAxisSpacing: AppSetting.setWidth(10),
        mainAxisSpacing: AppSetting.setWidth(10),
        childAspectRatio: 0.8,
      ),
      itemBuilder: (context, index) {
        return _buildImageCard(images[index]);
      },
    );
  }

  Widget _buildImageCard(File imageFile) {
    // Logic Parse Nama File
    final String rawFileName = imageFile.path.split('/').last;
    String displayId = "";
    String displayName = rawFileName;

    if (rawFileName.contains('-')) {
      final List<String> parts = rawFileName.split('-');
      displayId = parts.first;
      displayName = parts.sublist(1).join('-').replaceAll(RegExp(r'\.jpe?g', caseSensitive: false), '');
    }

    // --- FIX: Bungkus Container dengan InkWell dan Hero ---
    return Material(
      color: Colors.transparent,
      child: InkWell(
        onTap: () {
           // Panggil fungsi preview
           _showImagePreview(context, imageFile, displayName);
        },
        borderRadius: BorderRadius.circular(12),
        child: Container(
          decoration: BoxDecoration(
            color: Colors.white,
            borderRadius: BorderRadius.circular(12),
            boxShadow: const [BoxShadow(color: Colors.black12, blurRadius: 4, offset: Offset(0, 2))],
          ),
          child: Column(
            crossAxisAlignment: CrossAxisAlignment.stretch,
            children: [
              // Gambar
              Expanded(
                child: ClipRRect(
                  borderRadius: const BorderRadius.vertical(top: Radius.circular(12)),
                  child: Hero(
                    tag: imageFile.path, // Animasi transisi
                    child: Image.file(
                      imageFile,
                      fit: BoxFit.cover,
                      errorBuilder: (_, __, ___) => const Icon(Icons.broken_image),
                    ),
                  ),
                ),
              ),
              
              // Keterangan
              Padding(
                padding: const EdgeInsets.all(8),
                child: Column(
                  crossAxisAlignment: CrossAxisAlignment.start,
                  children: [
                    if (displayId.isNotEmpty)
                      Container(
                        padding: const EdgeInsets.symmetric(horizontal: 4, vertical: 2),
                        decoration: BoxDecoration(
                          color: Colors.grey[200],
                          borderRadius: BorderRadius.circular(4),
                        ),
                        child: Text(
                          displayId,
                          style: const TextStyle(fontSize: 10, fontWeight: FontWeight.bold, color: Colors.black54),
                        ),
                      ),
                    const SizedBox(height: 2),
                    Text(
                      displayName,
                      maxLines: 1,
                      overflow: TextOverflow.ellipsis,
                      style: const TextStyle(fontSize: 12, fontWeight: FontWeight.w600),
                    ),
                  ],
                ),
              ),
            ],
          ),
        ),
      ),
    );
  }

  Widget _buildEmptyState() {
    return SingleChildScrollView( // Agar bisa di-refresh
      physics: const AlwaysScrollableScrollPhysics(),
      child: SizedBox(
        height: MediaQuery.of(context).size.height * 0.7,
        child: Center(
          child: Column(
            mainAxisAlignment: MainAxisAlignment.center,
            children: [
              Icon(Icons.folder_off, size: 60, color: Colors.grey[400]),
              const SizedBox(height: 10),
              Text(
                _filterDate == null ? "Belum ada foto" : "Tidak ada foto di tanggal ini",
                style: TextStyle(color: Colors.grey[600]),
              ),
              if (_filterDate != null)
                TextButton(
                  onPressed: _resetFilter,
                  child: const Text("Reset Filter"),
                )
            ],
          ),
        ),
      ),
    );
  }
}