abstract class AuthException implements Exception {
  final String message;
  const AuthException(this.message);
}

class InvalidCredentialException extends AuthException {
  const InvalidCredentialException()
      : super('Username atau password salah');
}

class ServerException extends AuthException {
  const ServerException()
      : super('Server sedang bermasalah, coba lagi nanti');
}

class NetworkException extends AuthException {
  const NetworkException()
      : super('Koneksi internet bermasalah');
}
