using BusinessLogicInterface;
using DataAccessInterface;
using Domain;
using Newtonsoft.Json;
using System;
using System.Net.Http;
using System.Text;
using System.Threading.Tasks;

namespace BusinessLogic
{
    public class NotificationLogic : INotificationLogic
    {
        private readonly IDataAccess<AppUser> userRepo;

        public NotificationLogic(IDataAccess<AppUser> repo)
        {
            userRepo = repo;
        }

        public async Task SendNotificationAsync(Guid userId)
        {
            string userToken = GetUserIdFirebaseToken(userId);
            string[] tokens = { userToken };
            await SendPushNotification(tokens);
        }

        private string GetUserIdFirebaseToken(Guid userId)
        {
            var user = userRepo.Get(userId);
            return user.DeviceId;
        }

        public static async Task<bool> SendPushNotification(string[] deviceTokens)
        {
            Uri FireBasePushNotificationsURL = new Uri("https://fcm.googleapis.com/fcm/send");
            string ServerKey = "AAAABeQgjmo:APA91bE4oBFOvZavnD3UCOTvLraBzkkX1HG2FUAIBdn4gm6V0A38Acar_-EBxs1npsT1TQLkIoJ8wZb9dyjccyKEdnOytYmupe7wV9aaXfFQPk3x231Gy69NGe8PMUua4MZadsu9o3jn";

            bool sent = false;
            var messageInformation = new Message()
            {
                notification = new Notification()
                {
                    title = "Tu cuenta ha sido filtrada!",
                    body = "Para más información entra a la app"
                },
                registration_ids = deviceTokens,
                priority = "high"
            };
            string jsonMessage = JsonConvert.SerializeObject(messageInformation);
            var request = new HttpRequestMessage(HttpMethod.Post, FireBasePushNotificationsURL);
            request.Headers.TryAddWithoutValidation("Authorization", "key=" + ServerKey);
            request.Content = new StringContent(jsonMessage, Encoding.UTF8, "application/json");
            HttpResponseMessage result;
            using (var client = new HttpClient())
            {
                result = await client.SendAsync(request);
                sent = sent || result.IsSuccessStatusCode;
            }
            return sent;
        }
    }
}
