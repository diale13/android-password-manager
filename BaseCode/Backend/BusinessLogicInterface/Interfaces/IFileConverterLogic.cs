using Domain;
using System;
using System.Collections.Generic;

namespace BusinessLogicInterface.Interfaces
{
    public interface IFileConverterLogic
    {
        string GetExportedListAsZip(Guid userId);
    }
}
