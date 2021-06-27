using DataAccessInterface;
using Microsoft.EntityFrameworkCore;
using System;
using System.Collections.Generic;
using System.Data.Common;
using System.Linq;

namespace DataAccess
{
    public abstract class BaseRepo<T> : IDataAccess<T> where T : class
    {
        protected DbContext Context { get; set; }

        public virtual void Add(T entity)
        {
            try
            {
                Context.Set<T>().Add(entity);
                Context.SaveChanges();
            }
            catch (DbException)
            {
                throw new DataAccessException("Error: Could not add entity to DB");
            }
            catch (Exception e)
            {
                throw new DataAccessException(e.Message);
            }
        }

        public abstract T Get(Guid id);

        public virtual IEnumerable<T> GetAll()
        {
            try
            {
                return Context.Set<T>()
                    .ToList();
            }
            catch (DbException)
            {
                throw new DataAccessException("Error: could not get table elements");
            }
        }

        public virtual T GetByCondition(System.Linq.Expressions.Expression<Func<T, bool>> expression)
        {
            try
            {
                return Context.Set<T>().FirstOrDefault(expression);
            }
            catch (DbException)
            {
                throw new DataAccessException("Error: could not get table elements");
            }
        }

        public virtual void Remove(T entity)
        {
            try
            {
                Context.Set<T>().Remove(entity);
                Context.SaveChanges();
            }
            catch (DbUpdateException)
            {
                throw new DataAccessException("Error: entity to remove does not exist in context");
            }
            catch (DbException)
            {
                throw new DataAccessException("Error: could not get table elements");
            }
        }

        public virtual void Update(T entity)
        {
            try
            {
                Context.Entry(entity).State = EntityState.Modified;
                Context.Set<T>().Update(entity);
                Context.SaveChanges();
            }
            catch (DbUpdateException)
            {
                throw new DataAccessException("Error: entity to remove does not exist in context");
            }
            catch (DbException)
            {
                throw new DataAccessException("Error: could not get table elements");
            }
        }
    }
}
