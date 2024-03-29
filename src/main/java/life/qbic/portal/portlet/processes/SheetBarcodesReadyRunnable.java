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
package life.qbic.portal.portlet.processes;

import java.util.List;


import com.vaadin.server.FileDownloader;
import com.vaadin.server.FileResource;
import life.qbic.portal.portlet.io.BarcodeCreator;
import life.qbic.portal.portlet.model.IBarcodeBean;
import life.qbic.portal.portlet.model.Person;
import life.qbic.portal.portlet.view.BarcodeView;

/**
 * Class implementing the Runnable interface so it can trigger a response in the view after the
 * barcode creation thread finishes
 * 
 * @author Andreas Friedrich
 * 
 */
public class SheetBarcodesReadyRunnable implements Runnable {

  private BarcodeView view;
  private String projectCode;
  private String projectName;
  private Person investigator;
  private Person contact;
  private List<IBarcodeBean> barcodeBeans;
  private BarcodeCreator creator;

  public SheetBarcodesReadyRunnable(String projectCode, String projectName, Person investigator,
      Person contact, BarcodeView view, BarcodeCreator creator,
      List<IBarcodeBean> barcodeBeans) {
    this.view = view;
    this.projectCode = projectCode;
    this.projectName = projectName;
    this.investigator = investigator;
    this.contact = contact;
    this.barcodeBeans = barcodeBeans;
    this.creator = creator;
  }

  private void attachDownloadToButton() {
    FileResource sheetSource = creator.createAndDLSheet(projectCode, projectName, investigator,
        contact, barcodeBeans, view.getHeaders());
    FileDownloader sheetDL = new FileDownloader(sheetSource);
    sheetDL.extend(view.getDownloadButton());
  }

  @Override
  public void run() {
    attachDownloadToButton();
    view.creationDone();
    view.sheetReady();
  }

  @Override
  public String toString() {
    return "SheetBarcodesReadyRunnable{" +
            "view=" + view +
            ", projectCode='" + projectCode + '\'' +
            ", projectName='" + projectName + '\'' +
            ", investigator=" + investigator +
            ", contact=" + contact +
            ", barcodeBeans=" + barcodeBeans +
            ", creator=" + creator +
            '}';
  }
}
