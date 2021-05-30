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

Feature: U3 - Add participant to event

  Scenario: AC1 - Add participants to an event via a non-empty name
    Given There is a participant with the name "John Smith" and an existing event with name "AT Workshop" and date "07/06/2021"
    When I add that participant to the event
    Then I expect that event to contain the participant in its attending participants

  Scenario: AC2 - Adding participants that dont exist to an event, creates a participant with a given non-empty name.
    Given There is an existing event
    When I attempt to add participant to that event that doesn't exist
    Then A new participant should be added to that event with a given non-empty name

  Scenario: AC3 - Empty Participants or participants with names containing invalid characters cannot be added to events
    Given There is an existing event
    When I attempt to add an empty participant or participants with names "12345", " ", ""
    Then I should get an exception that disallows me to add any of those.




