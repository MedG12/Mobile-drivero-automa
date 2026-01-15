import 'package:dartz/dartz.dart';
import 'package:drivero_automa/core/errors/exceptions.dart';
import 'package:drivero_automa/data/datasources/local/auth_local_datasource.dart';
import 'package:drivero_automa/data/datasources/remote/auth_remote_datasource.dart';
import 'package:drivero_automa/data/models/user_model.dart';
import 'package:drivero_automa/domain/entities/user_entity.dart';
import 'package:drivero_automa/domain/repositories/auth_repository.dart';
import 'package:jwt_decoder/jwt_decoder.dart';

class AuthRepositoryImpl implements AuthRepository {
  final AuthRemoteDataSource remoteDataSource;
  final AuthLocalDataSource localDataSource;

  AuthRepositoryImpl(this.remoteDataSource, this.localDataSource);

  // =========================
  // LOGIN
  // =========================
  @override
  Future<Either<String, UserEntity>> login(
    String email,
    String password,
  ) async {
    try {
      final loginResponse = await remoteDataSource.login(email, password);
      final user = loginResponse.user;

      await localDataSource.saveToken(user.token);
      await localDataSource.saveUser(user);

      return Right(user);
    } on AuthException catch (e) {
      // ✅ pesan sudah user-friendly
      return Left(e.message);
    } catch (_) {
      // ✅ fallback aman
      return const Left('Terjadi kesalahan, coba lagi');
    }
  }

  // =========================
  // TOKEN
  // =========================
  @override
  Future<String?> getSavedToken() {
    return localDataSource.getToken();
  }

  // =========================
  // UPDATE BIODATA
  // =========================
  @override
  Future<Either<Exception, UserEntity>> updateBiodata(
    UserEntity data,
  ) async {
    try {
      final token = await localDataSource.getToken();
      if (token == null) {
        return Left(Exception('Token not found'));
      }

      final updatedUser = await remoteDataSource.updateUserBiodata(
        data.name,
        data.birthDate!,
        data.gender!,
        token,
      );

      if (updatedUser == null) {
        return Left(Exception('Gagal memperbarui biodata'));
      }

      await localDataSource.saveUser(
        UserModel(
          userId: updatedUser.userId,
          name: updatedUser.name,
          email: data.email,
          birthDate: updatedUser.birthDate,
          gender: updatedUser.gender,
          app: data.app,
          token: token,
          expires: data.expires,
        ),
      );

      return Right(updatedUser);
    } catch (e) {
      return Left(Exception(e.toString()));
    }
  }

  // =========================
  // GET SAVED USER
  // =========================
  @override
  Future<Either<String, UserEntity?>> getSavedUser() async {
    final token = await localDataSource.getToken();
    if (token == null || JwtDecoder.isExpired(token)) {
      return Right(null);
    }

    try {
      final user = await localDataSource.getUser();
      if (user == null) return Right(null);
      return Right(user);
    } catch (_) {
      return const Left('Gagal mengambil data user');
    }
  }

  // =========================
  // LOGOUT
  // =========================
  @override
  Future<Either<String, void>> logout() async {
    try {
      await localDataSource.clearToken();
      await localDataSource.clearUser();
      return Right(null);
    } catch (_) {
      return const Left('Gagal logout');
    }
  }
}
