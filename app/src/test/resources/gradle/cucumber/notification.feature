#
# Created on Wed Apr 09 2021
#
# The Unlicense
# This is free and unencumbered software released into the public domain.
#
# Anyone is free to copy, modify, publish, use, compile, sell, or distribute
# this software, either in source code form or as a compiled binary, for any
# purpose, commercial or non-commercial, and by any means.
#
# In jurisdictions that recognize copyright laws, the author or authors of this
# software dedicate any and all copyright interest in the software to the public
# domain. We make this dedication for the benefit of the public at large and to
# the detriment of our heirs and successors. We intend this dedication to be an
# overt act of relinquishment in perpetuity of all present and future rights to
# this software under copyright law.
#
# THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
# IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
# FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
# AUTHORS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN
# ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION
# WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
#
# For more information, please refer to <https://unlicense.org>
#

Feature: U4 - Notifying Participants of Status Changes

  Scenario: AC1 - Participants are notified of event status changes with a notification that contains
    event name and new status of an event.

    Given there is a scheduled event with name "Bobs Birthday"
    And that event has three participants
    When the status of that event changes to canceled
    Then I expect each participant to receive a notification containing the event name and new status


  Scenario: AC2 - When being notified of a status change, participant receives a message containing
    its name, event name and the new status of the event.

    Given there is a scheduled event with name "Freaky Friday"
    And there is a participant named "Sally" attending
    When the status of that event is changed to canceled
    Then I expect a notification message containing the participants name, the event name and its new status

  Scenario: AC3 - When a participant receives notification they should be allowed to remove themselves from that event
    Given there is a scheduled event with name "Event to Leave"
    And there is a participant named "John" attending
    When the status of that event is changed to canceled and the user wants to leave
    Then I expect that participant to be removed from that event.

  Scenario: AC4 - When a events status is changed to archived all participants are removed from the event.

    Given there is a canceled event with name "All Over"
    And that event has three participants
    When the status of that event changes to archived
    Then I expect there to be no participants attending that event
