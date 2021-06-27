using DataAccessInterface;
using Domain;
using Microsoft.EntityFrameworkCore;
using System;
using System.Collections.Generic;
using System.Data.Common;
using System.Linq;

namespace DataAccess
{
    public class EmailHolderQuery : IEmailHolderQuery
    {
        private DbContext Context { get; set; }
        public EmailHolderQuery(DbContext context)
        {
            this.Context = context;
        }

        public List<EmailHolder> GetHoldersByUser(Guid id)
        {
            try
            {
                return Context.Set<EmailHolder>().Where(a => a.OwnerUserId == id)
                                 .ToList();
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
