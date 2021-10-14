from kivy.app import App
from kivy.lang import Builder
from kivy.uix.screenmanager import  ScreenManager, Screen
from kivy.uix.button import Button
Builder.load_file('design.kv')



class LoginScreen(Screen):
    pass

class RootWidget(ScreenManager):
    pass

class MainApp(App):
    def build(self):
        return RootWidget()#Button(text='Hello World')

if __name__ == "__main__":
    MainApp().run()