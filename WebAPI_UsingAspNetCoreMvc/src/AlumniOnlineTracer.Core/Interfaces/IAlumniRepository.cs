using AlumniOnlineTracer.Core.Model;
using System;
using System.Collections.Generic;

namespace AlumniOnlineTracer.Core.Interfaces
{
	public interface IAlumniRepository
	{
		IEnumerable<AlumnusInfo> GetAll();

		void Add(AlumnusInfo alumnusInfo);

		AlumnusInfo GetAlumnusInfo(Guid id);

	}
}
