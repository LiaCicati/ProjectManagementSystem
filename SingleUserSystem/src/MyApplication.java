import Mediator.ProjectManagementModel;
import Mediator.ProjectManagementModelManager;
import View.ViewHandler;
import javafx.application.Application;
import javafx.stage.Stage;

public class MyApplication extends Application
{
  public void start(Stage primaryStage)
  {
    ProjectManagementModel model = new ProjectManagementModelManager();
    ViewHandler view = new ViewHandler(model);
    view.start(primaryStage);
  }

}
