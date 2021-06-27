using BusinessLogicInterface.Interfaces;
using System;
using SevenZip;
using DataAccessInterface;
using Domain;
using System.Collections.Generic;
using System.IO;
using System.Configuration;

namespace BusinessLogic
{
    public class FileLogic : IFileConverterLogic
    {
        private readonly IDataAccess<AppUser> userRepo;

        string routeAccountsFileTxt = @"C:\Temp\SavedList.txt";
        string routeOfZip = @"C:\Temp\ProtectedAccounts.zip";

        public FileLogic(IDataAccess<AppUser> repo)
        {
            this.userRepo = repo;
        }

        public string GetExportedListAsZip(Guid userId)
        {
            var user = userRepo.Get(userId);
            CreateTxt(user.Accounts);
            CompressProtectedZip(user);
            DeleteFile(routeAccountsFileTxt);
            string base64Zip = ConvertToBase64();
            // BringItBackToFile(base64Zip);  ---> OPCIONAL PARA VER QUE CONVIERTA BIEN A BASE64
            DeleteFile(routeOfZip);
            return base64Zip;
        }

        private void CreateTxt(List<Account> accounts)
        {
            TextWriter textWriter = new StreamWriter(routeAccountsFileTxt);

            var line = "";
            foreach (var account in accounts)
            {
                var userName = account.UserName;
                var accountSite = account.Site;
                var passwordOfSite = account.Password;
                var email = account.EmailHolder.Email;

                line = userName + ":" + accountSite + ":" + passwordOfSite + ":" + email;
                textWriter.WriteLine(line);
            }
            textWriter.Close();
        }

        private void CompressProtectedZip(AppUser user)
        {
            var sourceAccounts = routeAccountsFileTxt;
            string password = user.MainEmail;
            string destinationFile = routeOfZip;
            string sourceFiles = sourceAccounts;

            string workingDirectory = Environment.CurrentDirectory;
            string dllDirectory = Directory.GetParent(workingDirectory).FullName + "\\BusinessLogic\\Dlls\\7z.dll";

            // string dll = @"\..\Dlls\\7z.dll";
            SevenZipBase.SetLibraryPath(dllDirectory);

            SevenZipCompressor compressor = new SevenZipCompressor()
            {
                ArchiveFormat = OutArchiveFormat.SevenZip,
                CompressionMode = CompressionMode.Create,
                CompressionMethod = CompressionMethod.Lzma,
                CompressionLevel = CompressionLevel.Normal,
                TempFolderPath = Path.GetTempPath()
            };

            if (string.IsNullOrWhiteSpace(password))
            {
                compressor.CompressDirectory(sourceFiles, destinationFile);
            }
            else
            {
                compressor.EncryptHeaders = true;
                compressor.CompressFilesEncrypted(destinationFile, password, sourceFiles);
            }
        }

        private string ConvertToBase64()
        {
            byte[] bytes = File.ReadAllBytes(routeOfZip);
            string file = Convert.ToBase64String(bytes);
            return file;
        }

        // ---------- To use on the Android side ----------------------
        /*private static void BringItBackToFile(String base64Zip)
        {
            var path = @"C:\Users\juani\Desktop\caca\protectedzip.zip";
            byte[] bytes = Convert.FromBase64String(base64Zip);
            File.WriteAllBytes(path, bytes);
        }
        */

        private void DeleteFile(string routeFile)
        {
            if (File.Exists(routeFile))
            {
                File.Delete(Path.GetFullPath(routeFile));
            }
        }
    }
}
