package flintstones.gathering.cloud.view.layout;

import org.eclipse.rap.ui.interactiondesign.layout.model.ILayoutSetInitializer;
import org.eclipse.rap.ui.interactiondesign.layout.model.LayoutSet;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;

public class LogoInitializer implements ILayoutSetInitializer{
	
	@Override
	public void initializeLayoutSet(LayoutSet layoutSet) {
		String path = ILayoutSetConstants.IMAGE_PATH;
		layoutSet.addImagePath( ILayoutSetConstants.LOGO, path + "logo.png" ); //$NON-NLS-1$

		FormData fdLogo = new FormData();
		fdLogo.right = new FormAttachment( 100, -50 );
		fdLogo.top = new FormAttachment( 50, 32 );
		layoutSet.addPosition( ILayoutSetConstants.LOGO_POSITION, fdLogo );
	}
}
