using System.Collections.Concurrent;
using System.Collections.Generic;
using System.IO;
using SampleProject.Model;

using Newtonsoft.Json;

namespace SampleProject.Repositories
{
    public class PersonalInfoRepository : IPersonalInfoRepository
    {
        private static ConcurrentDictionary<string, PersonalInfo> _personalInfos;
        private static string personalInfosFileName = "PersonalInfos.txt";

        static PersonalInfoRepository()
        {
            _personalInfos = new ConcurrentDictionary<string, PersonalInfo>();
			
            if (!File.Exists(personalInfosFileName))
            {
                File.Create(personalInfosFileName);
            }

            string personalInfosJsonString = string.Empty;
            using (FileStream fileStream = File.OpenRead(personalInfosFileName))
            {
                using (StreamReader reader = new StreamReader(fileStream))
                {
                    personalInfosJsonString = reader.ReadToEnd();
                }
            }

            ICollection<PersonalInfo> listOfPersonalInfos;
            if (string.IsNullOrEmpty(personalInfosJsonString))
            {
                 listOfPersonalInfos = new List<PersonalInfo>();
			// 	string friendsFileName = "Friends";
			// 	using (FileStream fileStream = File.OpenRead(friendsFileName))
			// 	{
			// 		using (StreamReader reader = new StreamReader(fileStream))
			// 		{
			// 			personalInfosJsonString = reader.ReadToEnd();
			// 		}
			// 	}				
            }
			else {
				listOfPersonalInfos = JsonConvert.DeserializeObject<List<PersonalInfo>>(personalInfosJsonString);
			}
			
            foreach (PersonalInfo info in listOfPersonalInfos)
            {
                _personalInfos.AddOrUpdate(info.UserId, info, (key, oldValue) => info);
            }
        }

        public PersonalInfoRepository()
        {
        }

        public IEnumerable<PersonalInfo> GetAll()
        {
            return _personalInfos.Values;
        }

        public PersonalInfo Get(string id)
        {
            return _personalInfos.ContainsKey(id) ? _personalInfos[id] : null;
        }

        public void AddOrUpdate(PersonalInfo personalInfo)
        {
            _personalInfos.AddOrUpdate(personalInfo.UserId, personalInfo, (key, valoldValueue) => personalInfo);
        }

        public void Delete(string id)
        {
            PersonalInfo deletedPersonalInfo;
            _personalInfos.TryRemove(id, out deletedPersonalInfo);
        }

        public void Save()
        {
            string serializedPersonalInfos = JsonConvert.SerializeObject(_personalInfos.Values);
			using (FileStream fileStream = File.OpenWrite(personalInfosFileName))
			{
				using (StreamWriter writer = new StreamWriter(fileStream))
				{
					writer.Write(serializedPersonalInfos);
				}
			}
        }
    }
}
