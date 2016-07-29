package flintstones.gathering.cloud.view;

import java.util.List;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.rap.rwt.RWT;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.IViewSite;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.ViewPart;

import flintstones.gathering.cloud.controller.CloseViewAction;
import flintstones.gathering.cloud.controller.ExportProblemAction;
import flintstones.gathering.cloud.controller.ExportProblemActionMenu;
import flintstones.gathering.cloud.controller.OpenViewAction;
import flintstones.gathering.cloud.dao.DAOProblem;
import flintstones.gathering.cloud.model.Problem;
import flintstones.gathering.cloud.model.User;

public class ProblemsView extends ViewPart {

	public static final String ID = "flintstones.gathering.cloud.view.problemsView";

	private TableViewer viewer;

	@Override
	public void init(IViewSite site) throws PartInitException {
		super.init(site);
		RWT.getUISession().setAttribute(ID, this);
	}
	
	@SuppressWarnings("serial")
	class ViewContentProvider implements IStructuredContentProvider {

		public void inputChanged(Viewer v, Object oldInput, Object newInput) {
		}

		public void dispose() {
		}

		@SuppressWarnings("unchecked")
		public Object[] getElements(Object parent) {
			return ((List<Problem>) parent).toArray(new Problem[0]);
		}
	}

	@SuppressWarnings("serial")
	class ViewLabelProvider extends LabelProvider {

		public String getText(Object obj) {
			return ((Problem) obj).toString();
		}

		public Image getImage(Object obj) {
			return PlatformUI.getWorkbench().getSharedImages()
					.getImage(ISharedImages.IMG_OBJ_ELEMENT);
		}
	}

	private Object createModel() {
		User user = (User) RWT.getUISession().getAttribute("user");
		
		return DAOProblem.getDAO().getProblems(user);
	}

	public void createPartControl(Composite parent) {
		viewer = new TableViewer(parent, SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL | SWT.BORDER);
		viewer.setContentProvider(new ViewContentProvider());
		viewer.setLabelProvider(new ViewLabelProvider());
		hookListeners();

		RWT.getUISession().setAttribute(ID, this);
		refreshModel();
	}

	/**
	 * Passing the focus request to the viewer's control.
	 */
	public void setFocus() {
		viewer.getControl().setFocus();
	}

	@SuppressWarnings("serial")
	private void hookListeners() {
		viewer.addSelectionChangedListener(new ISelectionChangedListener() {
			@Override
			public void selectionChanged(SelectionChangedEvent event) {
				updateSelection();
			}
		});

		viewer.getTable().addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				IStructuredSelection selection = (IStructuredSelection) viewer
						.getSelection();
				if (!selection.isEmpty()) {
					if (MessageDialog.openConfirm(viewer.getTable().getShell(),
							"Eliminar problema", "¿Estás seguro?")) {
						DAOProblem.getDAO().removeProblem(
								(Problem) selection.getFirstElement());
						refreshModel();
					}
				}
			}
		});
	}

	@SuppressWarnings("unchecked")
	public void refreshModel() {
		viewer.setInput(createModel());
		List<Problem> input = (List<Problem>) viewer.getInput();
		if (input.isEmpty()) {
			viewer.setSelection(new StructuredSelection(), false);
			((ExportProblemAction) RWT.getUISession().getAttribute("export-action")).setEnabled(false);
			((ExportProblemActionMenu) RWT.getUISession().getAttribute("export-action-menu")).setEnabled(false);
		} else {
			viewer.setSelection(new StructuredSelection(viewer.getElementAt(0)), true);
		}
		updateSelection();
	}

	private void updateSelection() {
		IStructuredSelection selection = (IStructuredSelection) viewer
				.getSelection();
		Problem problem = null;
		if (!selection.isEmpty()) {
			problem = (Problem) selection.getFirstElement();
		}

		RWT.getUISession().setAttribute("problem", problem);

		if (problem != null) {
			OpenViewAction ova = new OpenViewAction(getSite().getWorkbenchWindow(), "problem", ProblemView.ID);
			ova.run();
		} else {
			CloseViewAction cva = new CloseViewAction(getSite().getWorkbenchWindow(), "problem", ProblemView.ID);
			cva.run();
		}
	}
}
