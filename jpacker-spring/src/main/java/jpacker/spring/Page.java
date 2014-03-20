package jpacker.spring;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;


/**
 * 分页对象.包含数据及分页信??. (copy from springside)
 * 
 * @author Sam
 */

public final class Page<T> implements Serializable {
	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 231152607479172128L;
  
  /**
   * 第一页的索引
   */
  public static final int FIRST_PAGE_INDEX = 1;

	/**
	 * 空Page对象.
	 */
	public static final Page EMPTY_PAGE = new Page();

	/**
	 * 当前页第�?��数据的位置，0
	 */
	private int startIndex;

	/**
	 * 每页的记录数
	 */
	private int pageSize = 6;

	/**
	 * 当前页中存放的记�?
	 */
	private List<T> data;

	/**
	 * 总记录数
	 */
	private long rows;
	

	/**
	 * 构�?方法，只构�?�?
	 */
	public Page() {
		this(0, 0, 10, Collections.<T> emptyList());
	}

	/**
	 * 
	 * @param startIndex  本页数据在数据库中的起始位置
	 * @param rows 数据库中总记录条�?
	 * @param pageSize 本页容量
	 * @param data 本页包含的数�?
	 */
	public Page(int startIndex, long rows, int pageSize, List<T> data) {
		this.pageSize = pageSize;
		this.startIndex = startIndex;
		this.rows = rows;
		this.data = data;
	}

	/**
	 * 取数据库中包含的总记录数
	 */
	public long getRows() {
		return this.rows;
	}

	/**
	 * 取�?共页�?
	 */
	public int getPages() {
		if (rows % pageSize == 0) {
			return (int)rows / pageSize;
		} else {
			return (int)rows / pageSize + 1;
		}
	}

	/**
	 * 取每页数据容�?
	 */
	public int getPageSize() {
		return pageSize;
	}

	/**
	 * 获取当前页码
	 * 
	 * @return 第一页是1，第二页2...
	 */
	public int getPageNo() {
		return (startIndex / pageSize) + 1;
	}

	public int getStartIndex(){
		return startIndex;
	}
	
	/**
	 * 是否有下�?��
	 */
	public boolean getHasNextPage() {
		return this.getPageNo() < this.getPages();
	}

	/**
	 * 是否有上�?��
	 */
	public boolean getHasPreviousPage() {
		return (this.getPageNo() > 1);
	}

	/**
	 * 获取任一页第1条数据的位置,startIndex
	 */
	public static int getStartOfPage(int pageNo, int pageSize) {
		return (pageNo - 1) * pageSize;
	}
	
	/**
	 * @return the data
	 */
  public List<T> getData() {
		return data;
	}
  
  public void setData(List<T> data) {
    this.data = data;
  }

	/**
	 * @see java.lang.Object#toString()
	 */
//	public String toString() {
//		return ToStringBuilder.reflectionToString(this,
//				ToStringStyle.MULTI_LINE_STYLE);
//	}
}
