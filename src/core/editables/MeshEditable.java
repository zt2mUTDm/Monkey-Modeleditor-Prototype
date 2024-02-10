package core.editables;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import com.jme3.scene.Mesh;
import com.jme3.scene.Node;
import com.jme3.scene.shape.Box;

import online.money_daisuki.api.base.DataSink;
import online.money_daisuki.api.base.Requires;

public class MeshEditable implements Editable {
	private final Node node;
	private final List<EditionMode> modes;
	
	private Mesh mesh;
	private final Collection<DataSink<? super MeshEditable>> meshChangedListeners;
	
	public MeshEditable(final Mesh mesh) {
		this.node = new Node();
		this.mesh = Requires.notNull(mesh);
		this.modes = new ArrayList<>();
		this.meshChangedListeners = new LinkedList<>();
	}
	
	protected void addEditionMode(final EditionMode mode) {
		this.modes.add(Requires.notNull(mode, "mode == null"));
	}
	
	@Override
	public void setSelected(final boolean b) {
		
	}
	
	@Override
	public String toString() {
		return("Mesh");
	}
	
	@Override
	public Collection<EditionMode> getEditionModes() {
		return(Collections.unmodifiableList(modes));
	}

	public Mesh createMesh() {
		return(mesh.clone());
	}
	
	public void setMesh(final Mesh mesh) {
		this.mesh = Requires.notNull(mesh, "mesh == null");
		fireMeshChangedListener();
	}
	
	public Mesh getMesh() {
		return(mesh);
	}
	
	public void addMeshChangedListener(final DataSink<? super MeshEditable> l) {
		meshChangedListeners.add(Requires.notNull(l, "l == null"));
	}
	public void removeMeshChangedListener(final DataSink<? super MeshEditable> l) {
		meshChangedListeners.remove(Requires.notNull(l, "l == null"));
	}
	private void fireMeshChangedListener() {
		for(final DataSink<? super MeshEditable> l:meshChangedListeners) {
			l.sink(this);
		}
	}
	
	public static MeshEditable valueOf(final Mesh mesh) {
		MeshEditable edit;
		if(mesh instanceof Box) {
			edit = BoxMeshEditable.valueOf((Box)mesh);
		} else {
			edit = new MeshEditable(mesh);
		}
		
		return(edit);
	}
}
