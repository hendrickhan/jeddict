/**
 * Copyright [2016] Gaurav Gupta
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package org.netbeans.db.modeler.specification.model.scene;

import java.awt.event.ActionEvent;
import java.awt.event.InputEvent;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import static java.util.stream.Collectors.toList;
import javax.swing.JMenuItem;
import javax.swing.KeyStroke;
import org.netbeans.db.modeler.core.widget.column.ColumnWidget;
import org.netbeans.db.modeler.core.widget.column.ForeignKeyWidget;
import org.netbeans.db.modeler.core.widget.flow.ReferenceFlowWidget;
import org.netbeans.db.modeler.core.widget.table.TableWidget;
import org.netbeans.db.modeler.spec.DBColumn;
import org.netbeans.db.modeler.spec.DBForeignKey;
import org.netbeans.db.modeler.spec.DBMapping;
import org.netbeans.db.modeler.spec.DBTable;
import org.netbeans.db.modeler.specification.model.util.SQLEditorUtil;
import org.netbeans.db.modeler.theme.DBColorScheme;
import org.netbeans.jpa.modeler.collaborate.issues.ExceptionUtils;
import org.netbeans.jpa.modeler.core.widget.FlowNodeWidget;
import org.netbeans.jpa.modeler.spec.JoinColumn;
import org.netbeans.jpa.modeler.spec.validator.column.JoinColumnValidator;
import org.netbeans.jpa.modeler.spec.validator.override.AssociationValidator;
import org.netbeans.jpa.modeler.spec.validator.override.AttributeValidator;
import org.netbeans.jpa.modeler.specification.model.scene.JPAModelerScene;
import org.netbeans.modeler.core.exception.InvalidElmentException;
import org.netbeans.modeler.core.scene.vmd.DefaultPModelerScene;
import org.netbeans.modeler.specification.model.document.IColorScheme;
import org.netbeans.modeler.specification.model.document.core.IBaseElement;
import org.netbeans.modeler.specification.model.document.widget.IBaseElementWidget;
import org.netbeans.modeler.specification.model.document.widget.IFlowEdgeWidget;
import org.netbeans.modeler.specification.model.document.widget.IFlowElementWidget;
import org.netbeans.modeler.specification.model.document.widget.IFlowNodeWidget;
import org.netbeans.modeler.widget.edge.vmd.PEdgeWidget;
import org.netbeans.modeler.widget.node.vmd.internal.PFactory;

public class DBModelerScene extends DefaultPModelerScene<DBMapping> {

    @Override
    public void deleteBaseElement(IBaseElementWidget baseElementWidget) {
        DBMapping entityMappingsSpec = this.getBaseElementSpec();
        if (baseElementWidget instanceof IFlowElementWidget) {
            if (baseElementWidget instanceof FlowNodeWidget) { //reverse ref
                FlowNodeWidget flowNodeWidget = (FlowNodeWidget) baseElementWidget;
                IBaseElement baseElementSpec = flowNodeWidget.getBaseElementSpec();
                if (baseElementWidget instanceof TableWidget) {
                    TableWidget<DBTable> tableWidget = (TableWidget) baseElementWidget;
                    tableWidget.setLocked(true); //this method is used to prevent from reverse call( Recursion call) //  Source-flow-target any of deletion will delete each other so as deletion prcedd each element locked
                    for (ForeignKeyWidget foreignKeyWidget : tableWidget.getForeignKeyWidgets()) {
                        foreignKeyWidget.getReferenceFlowWidget().stream().forEach(w -> {
                            ((ReferenceFlowWidget) w).remove();
                        });
                    }
                    tableWidget.setLocked(false);
                }
                entityMappingsSpec.removeBaseElement(baseElementSpec);
                flowNodeWidget.setFlowElementsContainer(null);
                this.flowElements.remove(flowNodeWidget);
            } else if (baseElementWidget instanceof IFlowEdgeWidget) {
                if (baseElementWidget instanceof ReferenceFlowWidget) {
                    ReferenceFlowWidget referenceFlowWidget = (ReferenceFlowWidget) baseElementWidget;
                    referenceFlowWidget.setLocked(true);
                    ForeignKeyWidget foreignKeyWidget = referenceFlowWidget.getSourceWidget();
                    foreignKeyWidget.remove();
                    ColumnWidget columnWidget = (ColumnWidget) referenceFlowWidget.getTargetWidget();
                    columnWidget.remove();
                    referenceFlowWidget.setLocked(false);
                    referenceFlowWidget.setFlowElementsContainer(null);
                    this.flowElements.remove(referenceFlowWidget);
                } else {
                    throw new InvalidElmentException("Invalid JPA Element");
                }

            } else {
                throw new InvalidElmentException("Invalid JPA Element");
            }

        }
    }

    @Override
    public void createBaseElement(IBaseElementWidget baseElementWidget) {
        String baseElementId;
        Boolean isExist = false;
        if (baseElementWidget instanceof IFlowElementWidget) {
            this.flowElements.add((IFlowElementWidget) baseElementWidget);
            if (baseElementWidget instanceof IFlowNodeWidget) { //reverse ref
                ((FlowNodeWidget) baseElementWidget).setFlowElementsContainer(this);
                baseElementId = ((FlowNodeWidget) baseElementWidget).getId();
                isExist = ((FlowNodeWidget) baseElementWidget).getNodeWidgetInfo().isExist();
            } else if (baseElementWidget instanceof IFlowEdgeWidget) { //reverse ref
                ((IFlowEdgeWidget) baseElementWidget).setFlowElementsContainer(this);
                baseElementId = ((IFlowEdgeWidget) baseElementWidget).getId();
                isExist = ((PEdgeWidget) baseElementWidget).getEdgeWidgetInfo().isExist();
            } else {
                throw new InvalidElmentException("Invalid JPA FlowElement : " + baseElementWidget);
            }
        } else {
            throw new InvalidElmentException("Invalid JPA Element");
        }

        if (!isExist) {

//            IRootElement rootElement = this.getBaseElementSpec();
//            IBaseElement baseElement = null;
//            if (baseElementWidget instanceof IFlowElementWidget) {
//                if (baseElementWidget instanceof IFlowNodeWidget) {
//                    if (baseElementWidget instanceof TableWidget) {
//                        baseElement = new Table(null);
//                    } else {
//                        throw new InvalidElmentException("Invalid JPA Task Element : " + baseElement);
//                    }
//                } else if (baseElementWidget instanceof IFlowEdgeWidget) {
//                    // skip don't need to create spec RelationFlowWidget, GeneralizationFlowWidget,EmbeddableFlowWidget
//                } else {
//                    throw new InvalidElmentException("Invalid JPA Element");
//                }
//            } else {
//                throw new InvalidElmentException("Invalid JPA Element");
//            }
//            if (baseElement != null) {
//                baseElementWidget.setBaseElementSpec(baseElement);
//                baseElement.setId(baseElementId);
//                rootElement.addBaseElement(baseElement);
//                ElementConfigFactory elementConfigFactory = this.getModelerFile().getVendorSpecification().getElementConfigFactory();
//                elementConfigFactory.initializeObjectValue(baseElement);
//            }
        } else if (baseElementWidget instanceof IFlowElementWidget) {
            if (baseElementWidget instanceof FlowNodeWidget) {
                FlowNodeWidget flowNodeWidget = (FlowNodeWidget) baseElementWidget;
                flowNodeWidget.setBaseElementSpec(flowNodeWidget.getNodeWidgetInfo().getBaseElementSpec());
            } else {
                throw new InvalidElmentException("Invalid JPA Element");
            }
        } else {
            throw new InvalidElmentException("Invalid JPA Element");
        }

    }

    @Override
    public IColorScheme getColorScheme() {
//        DBMapping entityMappings = this.getBaseElementSpec();
//        if (PFactory.getNetBeans60Scheme().getSimpleName().equals(entityMappings.getTheme())) {
//            return PFactory.getColorScheme(PFactory.getNetBeans60Scheme());
//        }  else if (PFactory.getMetroScheme().getSimpleName().equals(entityMappings.getTheme())) {
//            return PFactory.getColorScheme(PFactory.getMetroScheme());
//        }  else {
//            return PFactory.getColorScheme(PFactory.getMacScheme());
//        }
        return PFactory.getColorScheme(DBColorScheme.class);
    }

    @Override
    public void setColorScheme(Class<? extends IColorScheme> scheme) {
//        DBMapping entityMappings = this.getBaseElementSpec();
//        entityMappings.setTheme(scheme.getSimpleName());
    }

    @Override
    public Map<String, Class<? extends IColorScheme>> getColorSchemes() {
        Map<String, Class<? extends IColorScheme>> colorSchemes = new HashMap<>();
//        colorSchemes.put("Classic",PFactory.getNetBeans60Scheme());
        colorSchemes.put("Default", DBColorScheme.class);
//        colorSchemes.put("Mac", PFactory.getMacScheme());
        return colorSchemes;
    }

    @Override
    public void destroy() {
        try {
            if (this.getModelerFile().isLoaded() && this.getBaseElementSpec() != null) {
                this.getBaseElementSpec().getTables().stream().map(t -> t.getEntity()).forEach(e -> {
                    AttributeValidator.filter(e);
                    AssociationValidator.filter(e);
                });
                this.getBaseElementSpec().getTables().stream().flatMap(t -> t.getColumns().stream())
                        .filter(c -> c instanceof DBForeignKey).collect(toList())
                        .forEach((DBColumn column) -> {
                            List<JoinColumn> joinColumns;
                            JoinColumn joinColumn;
                            joinColumn = ((DBForeignKey) column).getJoinColumn();
                            joinColumns = ((DBForeignKey) column).getJoinColumns();
                            if (joinColumn != null && JoinColumnValidator.isEmpty(joinColumn)) {
                                joinColumns.remove(joinColumn);
                            }
                        });
            }
        } catch (Exception ex) {
            ExceptionUtils.printStackTrace(ex);
        }
    }

    @Override
    public void init() {
        super.init();
    }

    @Override
    protected List<JMenuItem> getPopupMenuItemList() {
        List<JMenuItem> menuList = super.getPopupMenuItemList();
        JMenuItem openSQLEditor = new JMenuItem("View SQL Query");
        openSQLEditor.setAccelerator(KeyStroke.getKeyStroke(Character.valueOf('Q'), InputEvent.CTRL_DOWN_MASK));
        openSQLEditor.addActionListener((ActionEvent e) -> {
            SQLEditorUtil.openEditor(DBModelerScene.this.getModelerFile(), DBModelerScene.this.getBaseElementSpec().getSQL());
        });

        menuList.add(0, openSQLEditor);
        return menuList;
    }

}
