using Domain;
using System.Collections.Generic;

namespace ILeakApi
{
    public interface ILeaksApi
    {
        // checks for leaks and adds them to account return list.
        List<Account> CheckAndAddNewLeaks(List<Account> accounts);
        void DummyPostLeak(DummyLeak leakToAdd);
         
    }
}
