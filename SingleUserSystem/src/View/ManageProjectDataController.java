package View;

import Mediator.ProjectManagementModel;
import Model.Project;
import Model.Requirement;
import Model.TeamMember;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.Region;

import java.util.Optional;

public class ManageProjectDataController
{
  @FXML private Label errorLabelTeamMember;
  @FXML private Label errorLabelRequirement;

  @FXML private TableView<TeamMemberViewModel> teamListTable;
  @FXML private TableColumn<TeamMemberViewModel, String> teamNameColumn;
  @FXML private TableColumn<TeamMemberViewModel, Number> teamIDColumn;
  @FXML private TableColumn<TeamMemberViewModel, String> teamRoleColumn;

  @FXML private TableView<RequirementViewModel> requirementListTable;
  @FXML private TableColumn<RequirementViewModel, Number> requirementIDColumn;
  @FXML private TableColumn<RequirementViewModel, String> userStoryColumn;
  @FXML private TableColumn<RequirementViewModel, String> typeColumn;
  @FXML private TableColumn<RequirementViewModel, Number> estimatedTimeColumn;
  @FXML private TableColumn<RequirementViewModel, String> responsibleMemberColumn;
  @FXML private TableColumn<RequirementViewModel, String> requirementDeadlineColumn;
  @FXML private TableColumn<RequirementViewModel, String> statusColumn;
  @FXML private TableColumn<RequirementViewModel, Number> hoursWorkedColumn;

  private Region root;
  private ProjectManagementModel model;
  private ViewHandler viewHandler;
  private ViewState viewState;
  private TeamMemberListViewModel teamMemberListViewModel;
  private RequirementListViewModel requirementListViewModel;

  public ManageProjectDataController()
  {
    // Called by FXMLLoader
  }

  public void init(ViewHandler viewHandler, ProjectManagementModel model,
      Region root, ViewState viewState)
  {
    this.model = model;
    this.viewHandler = viewHandler;
    this.viewState = viewState;
    this.root = root;
    this.teamMemberListViewModel = new TeamMemberListViewModel(model,
        viewState);
    this.requirementListViewModel = new RequirementListViewModel(model,
        viewState);
    reset();

    teamNameColumn
        .setCellValueFactory(cellData -> cellData.getValue().getNameProperty());
    teamIDColumn
        .setCellValueFactory(cellData -> cellData.getValue().getIdProperty());
    teamRoleColumn
        .setCellValueFactory(cellData -> cellData.getValue().getRoleProperty());

    teamListTable.setItems(teamMemberListViewModel.getList());

    requirementIDColumn.setCellValueFactory(
        cellData -> cellData.getValue().getRequirementIDProperty());
    userStoryColumn.setCellValueFactory(
        cellData -> cellData.getValue().getUserStoryProperty());
    typeColumn
        .setCellValueFactory(cellData -> cellData.getValue().getTypeProperty());
    estimatedTimeColumn.setCellValueFactory(
        cellData -> cellData.getValue().getEstimatedTimeProperty());
    responsibleMemberColumn.setCellValueFactory(
        cellData -> cellData.getValue().getResponsibleTeamMemberProperty());
    requirementDeadlineColumn.setCellValueFactory(
        cellData -> cellData.getValue().getDeadlineProperty());
    statusColumn.setCellValueFactory(
        cellData -> cellData.getValue().getStatusProperty());
    hoursWorkedColumn.setCellValueFactory(
        cellData -> cellData.getValue().getHoursWorkedProperty());

    requirementListTable.setItems(requirementListViewModel.getList());
  }

  public void reset()
  {
    errorLabelTeamMember.setText("");
    errorLabelRequirement.setText("");

    teamMemberListViewModel.update();
    requirementListViewModel.update();
  }

  public Region getRoot()
  {
    return root;
  }

  @FXML private void backButtonPressed()
  {
    viewState.setSelectedProject(-1);
    viewHandler.openView("projectList");
  }

  @FXML private void removeRequirementButtonPressed()
  {
    errorLabelRequirement.setText("");
    try
    {
      RequirementViewModel selectedItem = requirementListTable
          .getSelectionModel().getSelectedItem();
      boolean remove = confirmationDeleteRequirement();
      if (remove)
      {
        Requirement requirement = model.getAllProjects()
            .getProjectById(viewState.getSelectedProject()).getAllRequirements()
            .getByID(selectedItem.getRequirementIDProperty().get());
        model.getAllProjects().getProjectById(viewState.getSelectedProject())
            .removeRequirement(requirement);
        requirementListViewModel.remove(requirement);
        requirementListTable.getSelectionModel().clearSelection();
      }
    }
    catch (Exception e)
    {
      errorLabelRequirement
          .setText("Choose a requirement you wish to remove from the list");
    }
  }

  @FXML private void editRequirementButtonPressed()
  {
    try
    {
      RequirementViewModel selectedItem = requirementListTable
          .getSelectionModel().getSelectedItem();
      viewState.setSelectedRequirement(
          selectedItem.getRequirementIDProperty().getValue());
      viewHandler.openView("addEditRequirement");

    }
    catch (Exception e)
    {
      errorLabelRequirement.setText("Select a requirement from the list");
    }

  }

  @FXML private void manageRequirementDataButtonPressed()
  {
    try
    {
      RequirementViewModel selectedItem = requirementListTable
          .getSelectionModel().getSelectedItem();
      viewState.setSelectedRequirement(
          selectedItem.getRequirementIDProperty().getValue());
      viewHandler.openView("manageRequirementData");
    }
    catch (Exception e)
    {
      errorLabelRequirement.setText("Select a requirement from the list");
    }
  }

  @FXML private void addRequirementButtonPressed()
  {
    viewState.setSelectedRequirement(-1);
    viewHandler.openView("addEditRequirement");
  }

  @FXML private void addTeamMemberButtonPressed()
  {
    viewState.setSelectedTeamMember(-1);
    viewHandler.openView("addEditTeamMember");

  }

  @FXML private void removeTeamMemberButtonPressed()
  {
    errorLabelTeamMember.setText("");
    try
    {
      TeamMemberViewModel selectedItem = teamListTable.getSelectionModel()
          .getSelectedItem();
      boolean remove = confirmation();
      if (remove)
      {
        TeamMember teamMember = model.getAllProjects()
            .getProjectById(viewState.getSelectedProject()).getAllTeamMembers()
            .getTeamMemberById(selectedItem.getIdProperty().get());
        model.getAllProjects().getProjectById(viewState.getSelectedProject())
            .removeTeamMember(teamMember);
        teamMemberListViewModel.remove(teamMember);
        teamListTable.getSelectionModel().clearSelection();
      }
    }
    catch (Exception e)
    {
      errorLabelTeamMember
          .setText("Choose a team member you wish to remove from the list");
    }
  }

  private boolean confirmation()
  {
    int index = teamListTable.getSelectionModel().getSelectedIndex();
    TeamMemberViewModel selectedItem = teamListTable.getItems().get(index);
    if (index >= teamListTable.getItems().size())
    {
      return false;
    }
    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
    alert.setTitle("Confirmation");
    alert.setHeaderText(
        "Are you sure you wish to remove the following team member: "
            + selectedItem.getNameProperty().get() + "?");
    Optional<ButtonType> result = alert.showAndWait();
    return (result.isPresent() && (result.get() == ButtonType.OK));
  }

  private boolean confirmationDeleteRequirement()
  {
    int index = requirementListTable.getSelectionModel().getSelectedIndex();
    RequirementViewModel selectedItem = requirementListTable.getItems()
        .get(index);
    if (index >= requirementListTable.getItems().size())
    {
      return false;
    }
    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
    alert.setTitle("Confirmation");
    alert.setHeaderText(
        "Are you sure you want to remove the following requirement: "
            + selectedItem.getRequirementIDProperty().get() + "?");
    Optional<ButtonType> result = alert.showAndWait();
    return (result.isPresent() && (result.get() == ButtonType.OK));
  }

  @FXML private void editTeamMemberButtonPressed()
  {
    try
    {
      TeamMemberViewModel selectedItem = teamListTable.getSelectionModel()
          .getSelectedItem();
      viewState.setSelectedTeamMember(selectedItem.getIdProperty().getValue());
      viewHandler.openView("addEditTeamMember");

    }
    catch (Exception e)
    {
      errorLabelTeamMember.setText("Select a team member from the list");
    }
  }

}

