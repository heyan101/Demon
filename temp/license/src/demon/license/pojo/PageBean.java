package demon.license.pojo;

public class PageBean {

	private Long pageIndex; // 从第几页开始
	private Long pageSize; // 查询多少条记录
	//private int start;  // 从第几条数据开始查询
	
	
	
	public PageBean() {
		super();
	}

	public PageBean(Long pageIndex, Long pageSize) {
		super();
		this.pageIndex = pageIndex;
		this.pageSize = pageSize;
	}
	
	public Long getPageIndex() {
		return pageIndex;
	}
	public void setPageIndex(Long pageIndex) {
		this.pageIndex = pageIndex;
	}
	public Long getPageSize() {
		return pageSize;
	}
	public void setPageSize(Long pageSize) {
		this.pageSize = pageSize;
	}
	public Long getStart() {
		return (pageIndex-1)*pageSize;
	}
	
	
}
