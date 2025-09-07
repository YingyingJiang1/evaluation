            @Override
            public void onMessage(String channel, String message) {
              LOG.debug("Sentinel {} published: {}.", node, message);

              String[] switchMasterMsg = message.split(" ");

              if (switchMasterMsg.length > 3) {

                if (masterName.equals(switchMasterMsg[0])) {
                  initMaster(toHostAndPort(switchMasterMsg[3], switchMasterMsg[4]));
                } else {
                  LOG.debug(
                      "Ignoring message on +switch-master for master {}. Our master is {}.",
                      switchMasterMsg[0], masterName);
                }

              } else {
                LOG.error("Invalid message received on sentinel {} on channel +switch-master: {}.",
                    node, message);
              }
            }
