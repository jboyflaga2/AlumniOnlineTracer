using System;
using System.Collections.Generic;
using SampleProject.Model;

namespace SampleProject.Repositories
{
	public interface IPersonalInfoRepository
	{
		IEnumerable<PersonalInfo> GetAll();

		PersonalInfo Get(string id);

		void AddOrUpdate(PersonalInfo personalInfo);

        void Delete(string id);

		void Save();
	}
}
