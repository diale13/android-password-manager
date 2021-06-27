using System;
using System.Collections.Generic;
using System.Linq.Expressions;

namespace DataAccessInterface
{
    public interface IDataAccess<T>
    {
        T GetByCondition(Expression<Func<T, bool>> expression);

        T Get(Guid id);

        IEnumerable<T> GetAll();

        void Add(T entity);

        void Remove(T entity);

        void Update(T entity);
    }
}
