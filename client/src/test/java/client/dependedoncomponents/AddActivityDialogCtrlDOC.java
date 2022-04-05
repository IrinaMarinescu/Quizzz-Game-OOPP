package client.dependedoncomponents;

import client.scenes.AddActivityDialogCtrl;
import client.scenes.AdminInterfaceCtrl;
import client.utils.ServerUtils;

public class AddActivityDialogCtrlDOC extends AddActivityDialogCtrl {

    public AddActivityDialogCtrlDOC(AdminInterfaceCtrl adminInterfaceCtrl, ServerUtils serverUtils) {
        super(adminInterfaceCtrl, serverUtils);
        test = true;
    }
}
