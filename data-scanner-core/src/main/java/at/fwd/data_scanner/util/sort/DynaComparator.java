
package at.fwd.data_scanner.util.sort;

import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.StringTokenizer;

import org.apache.commons.beanutils.PropertyUtils;

@SuppressWarnings("rawtypes")
public class DynaComparator implements java.util.Comparator {

	Collection<Object> sortProperties = new ArrayList<Object>();

	public DynaComparator() {
	}

	public DynaComparator(SortProperty sortPropery) {
		addSortProperty(sortPropery);
	}

	public DynaComparator(SortProperty[] sortProperties) {
		if (sortProperties != null) {
			for (int i = 0; i < sortProperties.length; i++)
				addSortProperty(sortProperties[i]);
		}
	}

	public void addSortProperty(SortProperty prop) {
		sortProperties.add(prop);
	}

	public Collection<Object> getSortProperties() {
		return sortProperties;
	}

	public void setSortProperties(Collection<Object> sortProperties) {
		this.sortProperties = sortProperties;
	}

	@SuppressWarnings({ "unchecked" })
	public int compare(Object obj1, Object obj2) {

		int result = 0;

		for (Iterator<Object> it = sortProperties.iterator(); it.hasNext()
				&& result == 0;) {
			SortProperty prop = (SortProperty) it.next();

			String propertyname = prop.getAttribute();

			Comparable value1 = getProperty(obj1, propertyname);
			Comparable value2 = getProperty(obj2, propertyname);

			if (value1 instanceof String && value2 instanceof String
					&& prop.isIgnoreCase()) {
				value1 = ((String) value1).toLowerCase();
				value2 = ((String) value2).toLowerCase();
			}

			if (value1 == null && value2 != null)
				result = 1;
			else if (value1 != null && value2 == null)
				result = -1;
			else if (value1 == null && value2 == null)
				result = 0;
			else
				result = value1.compareTo(value2);

			result = result * prop.getMode();

		}
		return result;
	}

	private Comparable getProperty(Object o, String propertyname) {
		StringTokenizer st = new StringTokenizer(propertyname, ".");
		Object tmpObject = o;
		try {
			PropertyDescriptor propertyDescriptor = null;
			String nextElement = null;
			while (st.hasMoreElements()) {
				nextElement = (String)st.nextElement();
				if (tmpObject == null)
					return null;
				propertyDescriptor = PropertyUtils.getPropertyDescriptor(
						tmpObject, nextElement);
				tmpObject = propertyDescriptor.getReadMethod().invoke(
						tmpObject, (Object[])null);
			}
		} catch (IllegalAccessException e) {
			throw new IllegalArgumentException(propertyname + " is not a property of " + tmpObject.getClass());
		} catch (InvocationTargetException e) {
			throw new IllegalArgumentException(propertyname + " is not a property of " + tmpObject.getClass());
		} catch (NoSuchMethodException e) {
			throw new IllegalArgumentException(propertyname + " is not a property of " + tmpObject.getClass());
		}

		return (Comparable) tmpObject;
	}
}