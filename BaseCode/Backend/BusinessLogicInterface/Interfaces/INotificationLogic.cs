using System;
using System.Threading.Tasks;

namespace BusinessLogicInterface
{
    public interface INotificationLogic
    {
        Task SendNotificationAsync(Guid userId);
    }
}
