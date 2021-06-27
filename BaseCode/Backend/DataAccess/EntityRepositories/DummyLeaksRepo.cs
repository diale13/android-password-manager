using DataAccessInterface;
using Domain;
using Microsoft.EntityFrameworkCore;
using System;
using System.Data.Common;
using System.Linq;

namespace DataAccess
{
    public class DummyLeaksRepo : BaseRepo<DummyLeak>
    {
        public DummyLeaksRepo(DbContext context)
        {
            this.Context = context;
        }

        public override DummyLeak Get(Guid id)
        {
            try
            {
                return Context.Set<DummyLeak>().First(x => x.Id == id);
            }
            catch (DbException)
            {
                throw new DataAccessException("Error: could not get table elements");
            }
            catch (InvalidOperationException)
            {
                throw new DataAccessException("Error: leak not found");
            }
        }
    }
}
