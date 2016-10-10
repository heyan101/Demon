#coding:utf-8
# Created: 09/24/2014

import util

class AuthApi(util.ApiBase):

    MODULE_NAME = "auth"
    URL_PREFIX = "api"

    def login(self, account, password, type='name', **kwparams):
        return self._invoke("login", "", True, name=account, password=password, type=type, **kwparams)

    def checkLogin(self, token):
        return self._invoke("checkLogin", "", True, token=token)

    def forkToken(self, token):
        return self._invoke("forkToken", "", True, token=token)

    def logout(self, token):
        return self._invoke("logout", "", True, token=token)


