/*
 *      Copyright 2009-2011 Battams, Derek
 *       
 *       Licensed under the Apache License, Version 2.0 (the "License");
 *       you may not use this file except in compliance with the License.
 *       You may obtain a copy of the License at
 *
 *          http://www.apache.org/licenses/LICENSE-2.0
 *
 *       Unless required by applicable law or agreed to in writing, software
 *       distributed under the License is distributed on an "AS IS" BASIS,
 *       WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *       See the License for the specific language governing permissions and
 *       limitations under the License.
 */
package com.google.code.sagetvaddons.sagealert.client;

import java.util.ArrayList;
import java.util.List;

import com.extjs.gxt.ui.client.data.BaseListLoader;
import com.extjs.gxt.ui.client.data.BeanModel;
import com.extjs.gxt.ui.client.data.BeanModelReader;
import com.extjs.gxt.ui.client.data.ListLoadResult;
import com.extjs.gxt.ui.client.data.ListLoader;
import com.extjs.gxt.ui.client.data.RpcProxy;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.store.Record;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.MessageBox;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.form.TextField;
import com.extjs.gxt.ui.client.widget.grid.CellEditor;
import com.extjs.gxt.ui.client.widget.grid.CheckColumnConfig;
import com.extjs.gxt.ui.client.widget.grid.ColumnConfig;
import com.extjs.gxt.ui.client.widget.grid.ColumnData;
import com.extjs.gxt.ui.client.widget.grid.ColumnModel;
import com.extjs.gxt.ui.client.widget.grid.EditorGrid;
import com.extjs.gxt.ui.client.widget.grid.Grid;
import com.extjs.gxt.ui.client.widget.grid.GridCellRenderer;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.extjs.gxt.ui.client.widget.toolbar.ToolBar;
import com.google.code.sagetvaddons.sagealert.client.ClientListenerSubscriptionForm.MediaType;
import com.google.code.sagetvaddons.sagealert.shared.Client;
import com.google.code.sagetvaddons.sagealert.shared.ClientService;
import com.google.code.sagetvaddons.sagealert.shared.ClientServiceAsync;
import com.google.code.sagetvaddons.sagealert.shared.Client.EventType;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.rpc.AsyncCallback;


/**
 * A GUI widget for modifying client alias and notification settings
 * @author dbattams
 * @version $Id$
 */
final class ClientSettingsPanel extends ContentPanel {
	static private final ClientSettingsPanel INSTANCE = new ClientSettingsPanel();
	static final ClientSettingsPanel getInstance() { return INSTANCE; }
	
	private ClientServiceAsync rpc;
	private RpcProxy<List<Client>> proxy;
	private BeanModelReader reader;
	ListLoader<ListLoadResult<Client>> loader;
	private ListStore<BeanModel> store;
	private List<ColumnConfig> cols;
	private Grid<BeanModel> grid;	
	
	private GridCellRenderer<BeanModel> btnRenderer;
	
	/**
	 * Constructor
	 */
	private ClientSettingsPanel() {
		setHeading("Client Configuration");
		setLayout(new FitLayout());
		
		btnRenderer = new GridCellRenderer<BeanModel>() {

			public Object render(final BeanModel model, String property, final ColumnData config, int rowIndex, int colIndex, ListStore<BeanModel> store, Grid<BeanModel> grid) {
				Button btn = new Button("Edit", new SelectionListener<ButtonEvent>() {

					@Override
					public void componentSelected(ButtonEvent ce) {
						EventType type;
						if(config.id.endsWith("Start"))
							type = EventType.STARTS;
						else if(config.id.endsWith("Stop"))
							type = EventType.STOPS;
						else if(config.id.endsWith("Pause"))
							type = EventType.PAUSES;
						else
							type = EventType.RESUMES;
						MediaType mediaType;
						if(config.id.contains("Tv"))
							mediaType = MediaType.TV;
						else if(config.id.contains("Dvd"))
							mediaType = MediaType.DVD;
						else if(config.id.contains("Import"))
							mediaType = MediaType.IMPORT;
						else
							mediaType = MediaType.MUSIC;
						final ClientReporterConfigWindow w = new ClientReporterConfigWindow((Client)model.getBean(), type, mediaType);
						final MessageBox progressBar = MessageBox.wait("Loading configuration window...", "Please wait while the configuration window is loading...", null);
						new Timer() {

							@Override
							public void run() {
								if(w.isBuilt()) {
									this.cancel();
									progressBar.close();
									w.show();
									w.center();
								}
							}
							
						}.scheduleRepeating(500);
					}
					
				});
				return btn;
			}
			
		};
		rpc = GWT.create(ClientService.class);
		proxy = new RpcProxy<List<Client>>() {

			@Override
			protected void load(Object loadConfig, AsyncCallback<List<Client>> callback) {
				rpc.getClients(callback);
			}
			
		};
		reader = new BeanModelReader();
		loader = new BaseListLoader<ListLoadResult<Client>>(proxy, reader);
		store = new ListStore<BeanModel>(loader);
		loader.load();
		cols = new ArrayList<ColumnConfig>();
		cols.add(new ColumnConfig("id", "UI Context", 200));
		
		ColumnConfig cfg = new ColumnConfig("alias", "Alias", 120);
		cfg.setEditor(new CellEditor(new TextField<String>()));
		cols.add(cfg);
	
		cfg = new CheckColumnConfig("notifyOnTvStart", "Starts TV", 90);
		cfg.setRenderer(btnRenderer);
		cols.add(cfg);
		
		cfg = new CheckColumnConfig("notifyOnTvStop", "Stops TV", 90);
		cfg.setRenderer(btnRenderer);
		cols.add(cfg);
		
		cfg = new CheckColumnConfig("notifiyOnTvPause", "Pauses TV", 90);
		cfg.setRenderer(btnRenderer);
		cols.add(cfg);

		cfg = new CheckColumnConfig("notifiyOnTvResume", "Resumes TV", 90);
		cfg.setRenderer(btnRenderer);
		cols.add(cfg);

		cfg = new CheckColumnConfig("notifyOnDvdStart", "Starts DVD/BR", 90);
		cfg.setRenderer(btnRenderer);
		cols.add(cfg);
		
		cfg = new CheckColumnConfig("notifyOnDvdStop", "Stops DVD/BR", 90);
		cfg.setRenderer(btnRenderer);
		cols.add(cfg);

		cfg = new CheckColumnConfig("notifiyOnDvdPause", "Pauses DVD/BR", 90);
		cfg.setRenderer(btnRenderer);
		cols.add(cfg);

		cfg = new CheckColumnConfig("notifiyOnDvdResume", "Resumes DVD/BR", 90);
		cfg.setRenderer(btnRenderer);
		cols.add(cfg);

		cfg = new CheckColumnConfig("notifyOnImportStart", "Starts Import", 90);
		cfg.setRenderer(btnRenderer);
		cols.add(cfg);
		
		cfg = new CheckColumnConfig("notifyOnImportStop", "Stops Import", 90);
		cfg.setRenderer(btnRenderer);
		cols.add(cfg);

		cfg = new CheckColumnConfig("notifiyOnImportPause", "Pauses Import", 90);
		cfg.setRenderer(btnRenderer);
		cols.add(cfg);

		cfg = new CheckColumnConfig("notifiyOnImportResume", "Resumes Import", 90);
		cfg.setRenderer(btnRenderer);
		cols.add(cfg);

		cfg = new CheckColumnConfig("notifyOnMusicStart", "Starts Music", 90);
		cfg.setRenderer(btnRenderer);
		cols.add(cfg);
		
		cfg = new CheckColumnConfig("notifyOnMusicStop", "Stops Music", 90);
		cfg.setRenderer(btnRenderer);
		cols.add(cfg);

		cfg = new CheckColumnConfig("notifiyOnMusicPause", "Pauses Music", 90);
		cfg.setRenderer(btnRenderer);
		cols.add(cfg);

		cfg = new CheckColumnConfig("notifiyOnMusicResume", "Resumes Music", 90);
		cfg.setRenderer(btnRenderer);
		cols.add(cfg);

		ColumnModel cm = new ColumnModel(cols);
		grid = new EditorGrid<BeanModel>(store, cm);
		grid.setLazyRowRender(0);
		grid.setSize(620, 285);
		add(grid);
		
		ToolBar btns = new ToolBar();
		Button save = new Button("Save");
		save.addSelectionListener(new SelectionListener<ButtonEvent>() {

			@Override
			public void componentSelected(ButtonEvent ce) {
				List<Client> clnts = new ArrayList<Client>();
				for(Record r : store.getModifiedRecords())
					clnts.add((Client)((BeanModel)r.getModel()).getBean());
				rpc.saveClients(clnts, new AsyncCallback<Void>() {

					public void onFailure(Throwable caught) {
						MessageBox.alert("ERROR", caught.getLocalizedMessage(), null);
					}

					public void onSuccess(Void result) {
						store.commitChanges();
						MessageBox.alert("Completed", "Clients updated on the server successfully!", null);
					}
					
				});
			}
			
		});
		Button cancel = new Button("Cancel");
		cancel.addSelectionListener(new SelectionListener<ButtonEvent>() {

			@Override
			public void componentSelected(ButtonEvent ce) {
				store.rejectChanges();
			}
			
		});
		btns.add(save);
		btns.add(cancel);
		setBottomComponent(btns);
	}	
}
