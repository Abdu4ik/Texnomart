package uz.texnomart.bot;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendDocument;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import uz.texnomart.container.Container;
import uz.texnomart.controller.AdminController;
import uz.texnomart.controller.UserController;

public class MyBot extends TelegramLongPollingBot {
    @Override
    public String getBotToken() {
        return Container.TOKEN;
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage()) {
            if (Container.adminList.contains(String.valueOf(update.getMessage().getChatId()))) {
                AdminController.handleMessage(update);
            } else UserController.handleMessage(update);
        } else if (update.hasCallbackQuery()) {
            if (Container.adminList.contains(String.valueOf(update.getCallbackQuery().getMessage().getChatId())))
                AdminController.handleCallBackQuery(update.getCallbackQuery().getMessage(), update.getCallbackQuery().getData());
            else
                UserController.handleCallBackQuery(update.getCallbackQuery().getMessage(), update.getCallbackQuery().getData());
        }
    }

    @Override
    public String getBotUsername() {
        return Container.USERNAME;
    }

    public Message sendMsg(Object obj) {
        try {
            if (obj instanceof SendMessage) {
                execute((SendMessage) obj);
            } else if (obj instanceof DeleteMessage) {
                execute((DeleteMessage) obj);
            } else if (obj instanceof EditMessageText) {
                execute((EditMessageText) obj);
            } else if (obj instanceof SendPhoto) {
                return execute((SendPhoto) obj);
            }else if (obj instanceof SendDocument) {
                execute((SendDocument) obj);
            }
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
        return null;
    }
}
