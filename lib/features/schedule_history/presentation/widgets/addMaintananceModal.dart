import 'package:drivero_automa/config/app_config.dart';
import 'package:drivero_automa/theme/text_theme.dart';
import 'package:drivero_automa/theme/theme.dart';
import 'package:flutter/material.dart';

class AddMaintenanceModal {
  static void show(BuildContext context) {
    final _formKey = GlobalKey<FormState>();

    final TextEditingController eventController = TextEditingController();
    final TextEditingController dateController = TextEditingController();
    final TextEditingController locationController = TextEditingController();
    final TextEditingController feeController = TextEditingController();

    String? selectedVehicle;
    String? selectedMaintenance;

    showDialog(
      context: context,
      barrierDismissible: false,
      builder: (context) {
        return Dialog(
          shape: RoundedRectangleBorder(
            borderRadius: BorderRadius.circular(20),
          ),
          child: Padding(
            padding: EdgeInsets.only(
              left: 16,
              right: 16,
              top: 20,
              bottom: MediaQuery.of(context).viewInsets.bottom + 16,
            ),
            child: SingleChildScrollView(
              child: Form(
                key: _formKey,
                child: Column(
                  crossAxisAlignment: CrossAxisAlignment.start,
                  mainAxisSize: MainAxisSize.min,
                  children: [
                    // Header
                    Row(
                      mainAxisAlignment: MainAxisAlignment.spaceBetween,
                      children: [
                        Text(
                          "Add Maintenance",
                          style: AppTextTheme.headline3.copyWith(
                            color: MyTheme.color.primary,
                          ),
                        ),
                        IconButton(
                          icon: const Icon(Icons.close),
                          onPressed: () => Navigator.pop(context),
                        ),
                      ],
                    ),
                    Divider(color: MyTheme.color.grey, thickness: 1),
                    const SizedBox(height: 16),

                    // Event Name
                    Text(
                      "Event Name",
                      style: AppTextTheme.headline4.copyWith(
                        color: MyTheme.color.primary,
                      ),
                    ),
                    const SizedBox(height: 6),
                    TextFormField(
                      controller: eventController,
                      decoration: InputDecoration(
                        isDense: true,
                        contentPadding: const EdgeInsets.symmetric(
                          vertical: 8,
                          horizontal: 12,
                        ),
                        hintStyle: AppTextTheme.bodyText1.copyWith(
                          color: MyTheme.color.primary,
                        ),
                        hintText: "Enter Event Name",
                        border: OutlineInputBorder(
                          borderRadius: BorderRadius.circular(10),
                          borderSide: BorderSide(color: MyTheme.color.grey),
                        ),
                      ),
                      validator: (value) =>
                          value == null || value.isEmpty ? "Required" : null,
                    ),
                    const SizedBox(height: 16),

                    // Vehicle Type Dropdown
                    Text(
                      "Vehicle Type",
                      style: AppTextTheme.headline4.copyWith(
                        color: MyTheme.color.primary,
                      ),
                    ),
                    const SizedBox(height: 6),
                    DropdownButtonFormField<String>(
                      style: AppTextTheme.bodyText1.copyWith(
                        color: MyTheme.color.primary,
                      ),
                      decoration: InputDecoration(
                        isDense: true,
                        contentPadding: const EdgeInsets.symmetric(
                          vertical: 8,
                          horizontal: 12,
                        ),
                        hintStyle: AppTextTheme.bodyText1.copyWith(
                          color: MyTheme.color.primary,
                        ),
                        hintText: "Vehicle Type",
                        border: OutlineInputBorder(
                          borderRadius: BorderRadius.circular(10),
                          borderSide: BorderSide(color: MyTheme.color.grey),
                        ),
                      ),
                      items: const [
                        DropdownMenuItem(value: "Car", child: Text("Car")),
                        DropdownMenuItem(
                          value: "Motorcycle",
                          child: Text("Motorcycle"),
                        ),
                      ],
                      onChanged: (val) {
                        selectedVehicle = val;
                      },
                      validator: (value) =>
                          value == null ? "Please select vehicle" : null,
                    ),
                    const SizedBox(height: 16),

                    // Maintenance Type Dropdown
                    Text(
                      "Maintenance Type",
                      style: AppTextTheme.headline4.copyWith(
                        color: MyTheme.color.primary,
                      ),
                    ),
                    const SizedBox(height: 6),
                    DropdownButtonFormField<String>(
                      style: AppTextTheme.bodyText1.copyWith(
                        color: MyTheme.color.primary,
                      ),
                      decoration: InputDecoration(
                        isDense: true,
                        contentPadding: const EdgeInsets.symmetric(
                          vertical: 8,
                          horizontal: 12,
                        ),
                        hintStyle: AppTextTheme.bodyText1.copyWith(
                          color: MyTheme.color.primary,
                        ),
                        hintText: "Maintenance Type",
                        border: OutlineInputBorder(
                          borderRadius: BorderRadius.circular(10),
                          borderSide: BorderSide(color: MyTheme.color.grey),
                        ),
                      ),
                      items: const [
                        DropdownMenuItem(
                          value: "Oil Change",
                          child: Text("Oil Change"),
                        ),
                        DropdownMenuItem(
                          value: "Tire Replacement",
                          child: Text("Tire Replacement"),
                        ),
                      ],
                      onChanged: (val) {
                        selectedMaintenance = val;
                      },
                      validator: (value) =>
                          value == null ? "Please select type" : null,
                    ),
                    const SizedBox(height: 16),

                    // Date & Time
                    Text(
                      "Date & Time",
                      style: AppTextTheme.headline4.copyWith(
                        color: MyTheme.color.primary,
                      ),
                    ),
                    const SizedBox(height: 6),
                    TextFormField(
                      controller: dateController,
                      readOnly: true,
                      decoration: InputDecoration(
                        isDense: true,
                        contentPadding: const EdgeInsets.symmetric(
                          vertical: 8,
                          horizontal: 12,
                        ),
                        hintStyle: AppTextTheme.bodyText1.copyWith(
                          color: MyTheme.color.primary,
                        ),
                        hintText: "dd/mm/yy",
                        prefixIcon: const Icon(Icons.calendar_today),
                        border: OutlineInputBorder(
                          borderRadius: BorderRadius.circular(10),
                          borderSide: BorderSide(color: MyTheme.color.grey),
                        ),
                      ),
                      validator: (value) =>
                          value == null || value.isEmpty ? "Required" : null,
                      onTap: () async {
                        final date = await showDatePicker(
                          context: context,
                          firstDate: DateTime.now(),
                          lastDate: DateTime(2100),
                          initialDate: DateTime.now(),
                        );
                        if (date != null) {
                          dateController.text =
                              "${date.day}/${date.month}/${date.year}";
                        }
                      },
                    ),
                    const SizedBox(height: 16),

                    // Location Name
                    Text(
                      "Location Name",
                      style: AppTextTheme.headline4.copyWith(
                        color: MyTheme.color.primary,
                      ),
                    ),
                    TextFormField(
                      controller: locationController,
                      decoration: InputDecoration(
                        isDense: true,
                        contentPadding: const EdgeInsets.symmetric(
                          vertical: 8,
                          horizontal: 12,
                        ),
                        hintStyle: AppTextTheme.bodyText1.copyWith(
                          color: MyTheme.color.primary,
                        ),
                        hintText: "Enter Location Name",
                        border: OutlineInputBorder(
                          borderRadius: BorderRadius.circular(10),
                          borderSide: BorderSide(color: MyTheme.color.grey),
                        ),
                      ),
                      validator: (value) =>
                          value == null || value.isEmpty ? "Required" : null,
                    ),
                    const SizedBox(height: 16),

                    // Maintenance Fee
                    Text(
                      "Maintenance Fee",
                      style: AppTextTheme.headline4.copyWith(
                        color: MyTheme.color.primary,
                      ),
                    ),
                    const SizedBox(height: 6),
                    TextFormField(
                      controller: feeController,
                      keyboardType: TextInputType.number,
                      decoration: InputDecoration(
                        isDense: true,
                        contentPadding: const EdgeInsets.symmetric(
                          vertical: 8,
                          horizontal: 12,
                        ),
                        hintStyle: AppTextTheme.bodyText1.copyWith(
                          color: MyTheme.color.primary,
                        ),
                        hintText: "Enter Maintenance Fee",
                        border: OutlineInputBorder(
                          borderRadius: BorderRadius.circular(10),
                          borderSide: BorderSide(color: MyTheme.color.grey),
                        ),
                      ),
                      validator: (value) =>
                          value == null || value.isEmpty ? "Required" : null,
                    ),
                    const SizedBox(height: 20),

                    // Submit Button
                    SizedBox(
                      width: double.infinity,
                      child: ElevatedButton(
                        style: ElevatedButton.styleFrom(
                          backgroundColor: MyTheme.color.primary,
                          foregroundColor: MyTheme.color.white,
                          shape: RoundedRectangleBorder(
                            borderRadius: BorderRadius.circular(24),
                          ),
                          padding: const EdgeInsets.symmetric(vertical: 14),
                        ),
                        onPressed: () {
                          if (_formKey.currentState!.validate()) {
                            // ✅ Ambil semua data form di sini
                            final data = {
                              "event": eventController.text,
                              "vehicle": selectedVehicle,
                              "maintenance": selectedMaintenance,
                              "date": dateController.text,
                              "location": locationController.text,
                              "fee": feeController.text,
                            };
                            print("Submit Data: $data");

                            Navigator.pop(context);
                          }
                        },
                        child: const Text("Submit"),
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
