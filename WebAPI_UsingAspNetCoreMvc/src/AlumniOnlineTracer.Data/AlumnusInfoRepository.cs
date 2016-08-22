using AlumniOnlineTracer.Core.Interfaces;
using AlumniOnlineTracer.Core.Model;
using System;
using System.Collections.Concurrent;
using System.Collections.Generic;

namespace AlumniOnlineTracer.Data
{
	public class AlumniRepository : IAlumniRepository
	{
		private static ConcurrentDictionary<Guid, AlumnusInfo> _alumni;

		static AlumniRepository()
		{
			_alumni = new ConcurrentDictionary<Guid, AlumnusInfo>();

			var newAlumnusInfo = new AlumnusInfo { FirstName = "Jboy", LastName = "Flaga" };
			_alumni.AddOrUpdate(newAlumnusInfo.Id, newAlumnusInfo, (key, odValue) => newAlumnusInfo);

			newAlumnusInfo = new AlumnusInfo { FirstName = "Donald", LastName = "Magallena" };
			_alumni.AddOrUpdate(newAlumnusInfo.Id, newAlumnusInfo, (key, odValue) => newAlumnusInfo);
		}

		public AlumniRepository()
		{
		}

		public IEnumerable<AlumnusInfo> GetAll()
		{
			return _alumni.Values;
		}

		public void Add(AlumnusInfo alumnusInfo)
		{
			if(!_alumni.ContainsKey(alumnusInfo.Id))
			{
				_alumni[alumnusInfo.Id] = alumnusInfo;
			}
		}

		public AlumnusInfo GetAlumnusInfo(Guid id)
		{
			return _alumni.ContainsKey(id) ? _alumni[id] : null;
		}
	}
}
