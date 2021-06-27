using System;

namespace SessionInterface
{
    public interface ISessionManager
    {
        string GetApiKey();
        bool IsValidToken(string token);
        Guid? CreateToken(string email, string password);
        Guid GetUser(string token);
    }
}
