package gatech.edu.DeathRecordPuller.ID.model;

import java.util.List;

public class IDList {
	private int count;
	private String created;
	private List<IDEntry> list;
	public int getCount() {
		return count;
	}
	public void setCount(int count) {
		this.count = count;
	}
	public String getCreated() {
		return created;
	}
	public void setCreated(String created) {
		this.created = created;
	}
	public List<IDEntry> getList() {
		return list;
	}
	public void setList(List<IDEntry> list) {
		this.list = list;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + count;
		result = prime * result + ((created == null) ? 0 : created.hashCode());
		result = prime * result + ((list == null) ? 0 : list.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		IDList other = (IDList) obj;
		if (count != other.count)
			return false;
		if (created == null) {
			if (other.created != null)
				return false;
		} else if (!created.equals(other.created))
			return false;
		if (list == null) {
			if (other.list != null)
				return false;
		} else if (!list.equals(other.list))
			return false;
		return true;
	}
	
	@Override
	public String toString() {
		return "IDList [count=" + count + ", created=" + created + ", list=" + list + "]";
	}
	
}
