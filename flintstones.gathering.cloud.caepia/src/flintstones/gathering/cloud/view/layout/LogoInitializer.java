package flintstones.gathering.cloud.view.layout;

import org.eclipse.rap.internal.design.example.ILayoutSetConstants;
import org.eclipse.rap.ui.interactiondesign.layout.model.ILayoutSetInitializer;
import org.eclipse.rap.ui.interactiondesign.layout.model.LayoutSet;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;

public class LogoInitializer implements ILayoutSetInitializer {
	
	public LogoInitializer() {}

	public void initializeLayoutSet(LayoutSet layoutSet) {
		layoutSet.addImagePath(ILayoutSetConstants.LOGO, ILayoutSetConstants.IMAGE_PATH_BUSINESS + "logo.png");

		// positions
		FormData fdLogo = new FormData();
		fdLogo.right = new FormAttachment(100, -75);
		fdLogo.top = new FormAttachment(0, 32);
		layoutSet.addPosition(ILayoutSetConstants.LOGO_POSITION, fdLogo);
	}
}
