/*******************************************************************************
 * QBiC Project Wizard enables users to create hierarchical experiments including different study
 * conditions using factorial design. Copyright (C) "2016" Andreas Friedrich
 *
 * This program is free software: you can redistribute it and/or modify it under the terms of the
 * GNU General Public License as published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without
 * even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with this program. If
 * not, see <http://www.gnu.org/licenses/>.
 *******************************************************************************/
package life.qbic.portal.portlet.sorters;

import java.util.Comparator;

import life.qbic.portal.portlet.model.IBarcodeBean;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
/**
 * Compares IBarcodeBeans by sample ID
 * 
 * @author Andreas Friedrich
 *
 */
public class SampleCodeComparator implements Comparator<IBarcodeBean> {

  private static final Logger LOG = LogManager.getLogger(SampleCodeComparator.class);

  private static final SampleCodeComparator instance = new SampleCodeComparator();

  public static SampleCodeComparator getInstance() {
    return instance;
  }

  private SampleCodeComparator() {}

  @Override
  public int compare(IBarcodeBean o1, IBarcodeBean o2) {
    String c1 = o1.getCode();
    String c2 = o2.getCode();
    if (!c1.startsWith("2") || c1.contains("ENTITY") || !c2.startsWith("2") //changed by CFH starts with "Q" to starts with "2"
        || c2.contains("ENTITY"))
      return o1.getCode().compareTo(o2.getCode());
    try {
      // compares sample codes by projects, ending letters (999A --> 001B) and numbers (001A -->
      // 002A)
      int projCompare = c1.substring(0, 16).compareTo(c2.substring(0, 16)); //changed by CFH  from (0, 5) to (0, 16)
      int numCompare = c1.substring(16, 19).compareTo(c2.substring(16, 19)); // 5 to 8 was original values
      int letterCompare = c1.substring(19, 20).compareTo(c2.substring(19, 20)); //8 , 9 original values
      if (projCompare != 0)
        return projCompare;
      else {
        if (letterCompare != 0)
          return letterCompare;
        else
          return numCompare;
      }
    } catch (Exception e) {
      LOG.warn("Could not split code " + c1 + " or " + c2
          + ". Falling back to primitive lexicographical comparison.");
    }

    return o1.getCode().compareTo(o2.getCode());
  }
}
