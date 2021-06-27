using Domain;
using Microsoft.EntityFrameworkCore;

namespace DataAccess
{
    public class Context : DbContext
    {
        public DbSet<Account> Accounts { get; set; }
        public DbSet<AppUser> Users { get; set; }
        public DbSet<EmailHolder> Emails { get; set; }
        public DbSet<LeakedSite> LeakedSites { get; set; }
      
        //Delete after getting real leaks api
        public DbSet<DummyLeak> DummyLeaks { get; set; }


        public Context(DbContextOptions options) : base(options)
        {

        }

        protected override void OnModelCreating(ModelBuilder modelBuilder)
        {
            base.OnModelCreating(modelBuilder);

            modelBuilder.Entity<Account>().HasOne(a => a.EmailHolder);
            modelBuilder.Entity<EmailHolder>().HasMany(e => e.LeakedSites);
            modelBuilder.Entity<AppUser>().HasMany(a => a.Accounts);

        }

    }
}

