using DataAccessInterface;
using Domain;
using Microsoft.EntityFrameworkCore;
using System;
using System.Data.Common;
using System.Linq;

namespace DataAccess
{
    public class AppUserRepo : BaseRepo<AppUser>
    {
        public AppUserRepo(DbContext context)
        {
            this.Context = context;
        }

        public override AppUser Get(Guid id)
        {
            try
            {
                return Context.Set<AppUser>().Include(a => a.Accounts)
                                             .ThenInclude(b => b.EmailHolder)
                                             .ThenInclude(c => c.LeakedSites)
                                             .First(x => x.Id == id);
            }
            catch (DbException)
            {
                throw new DataAccessException("Error: could not get table elements");
            }
            catch (InvalidOperationException)
            {
                throw new DataAccessException("Error: user not found");
            }
        }
    }
}
