using Domain;
using System;
using System.Collections.Generic;

namespace BusinessLogicInterface
{
    public interface ILeakLogic
    {
        List<LeakContainer> GetNewLeaks(Guid userId);
        List<LeakContainer> GetAllLeaks(Guid userId);
        void CreateDummy(DummyLeak l);

    }
}
