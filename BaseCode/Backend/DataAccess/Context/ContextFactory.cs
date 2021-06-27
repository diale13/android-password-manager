using Microsoft.EntityFrameworkCore;
using Microsoft.EntityFrameworkCore.Design;
using Microsoft.Extensions.Configuration;
using System.IO;

namespace DataAccess
{

    public enum ContextType
    {
        MEMORY, DATABASE
    }

    public class ContextFactory : IDesignTimeDbContextFactory<Context>
    {

        public Context GetNewContext(ContextType type = ContextType.DATABASE)
        {
            var builder = new DbContextOptionsBuilder<Context>();
            DbContextOptions options;
            if (type == ContextType.MEMORY)
            {
                options = GetMemoryConfig(builder);
            }
            else
            {
                options = GetDatabaseConfig(builder);
            }
            return new Context(options);
        }

        private DbContextOptions GetMemoryConfig(DbContextOptionsBuilder builder)
        {
            builder.UseInMemoryDatabase("dataBaseMemory");
            return builder.Options;
        }

        private DbContextOptions GetDatabaseConfig(DbContextOptionsBuilder builder)
        {
            var conectionString = GetConnectionString();
            builder.UseLazyLoadingProxies(false);
            builder.UseSqlServer(conectionString);
            return builder.Options;
        }

        public static string GetConnectionString()
        {
            string directory = Directory.GetCurrentDirectory();
            IConfigurationRoot configuration = new ConfigurationBuilder()
            .SetBasePath(directory)
            .AddJsonFile("appsettings.json")
            .Build();
            return configuration.GetConnectionString(@"Db");
        }

        public Context CreateDbContext(string[] args)
        {
            return GetNewContext();
        }
    }
}

