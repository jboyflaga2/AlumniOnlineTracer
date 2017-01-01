using System;
using System.Collections.Concurrent;
using System.Collections.Generic;
using SampleProject.Model;

namespace SampleProject.Repositories
{
	public class PersonalInfoRepository : IPersonalInfoRepository
	{
		private static ConcurrentDictionary<string, PersonalInfo> _personalInfos;

		static PersonalInfoRepository()
		{
			_personalInfos = new ConcurrentDictionary<string, PersonalInfo>();

			var newPersonalInfo = new PersonalInfo("jboyflaga") { FirstName = "Jboy", LastName = "Flaga" };
			_personalInfos.AddOrUpdate(newPersonalInfo.Id, newPersonalInfo, (key, oldValue) => newPersonalInfo);

			newPersonalInfo = new PersonalInfo("donald") { FirstName = "Donald", LastName = "Magallena" };
			_personalInfos.AddOrUpdate(newPersonalInfo.Id, newPersonalInfo, (key, oldValue) => newPersonalInfo);
            
			newPersonalInfo = new PersonalInfo("johnmichael") { FirstName = "John Michael", LastName = "Sabnal" };
			_personalInfos.AddOrUpdate(newPersonalInfo.Id, newPersonalInfo, (key, oldValue) => newPersonalInfo);
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
			_personalInfos.AddOrUpdate(personalInfo.Id, personalInfo, (key, valoldValueue) => personalInfo);
		}

        public void Delete(string id) {
            PersonalInfo deletedPersonalInfo;
            _personalInfos.TryRemove(id, out deletedPersonalInfo);
        }
	}
}
