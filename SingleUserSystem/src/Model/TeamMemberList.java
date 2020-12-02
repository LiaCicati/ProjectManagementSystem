package Model;

import java.util.ArrayList;

public class TeamMemberList
{
  private ArrayList<TeamMember> teamMembers;

  public TeamMemberList()
  {
    teamMembers = new ArrayList<>();
  }

  public ArrayList<TeamMember> getTeamMembers(int index)
  {
    ArrayList a = new ArrayList();
    for(int i=0;i<teamMembers.size();i++)
    {
      a.add(teamMembers.get(i));

    }
    return a;
  }

  public void addTeamMember(TeamMember teamMember)
  {
    teamMembers.add(teamMember);
  }

  public void removeTeamMember(TeamMember teamMember)
  {
    teamMembers.remove(teamMember);
  }

  public TeamMember getTeamMemberByName(Name name)
  {
    for(int i=0;i<teamMembers.size();i++)
    {
      if(teamMembers.get(i).getName()==name)
      {
        return teamMembers.get(i);
      }
    }
    return null;
  }

  public TeamMember getTeamMemberById(int id)
  {
    for (int i = 0; i < teamMembers.size(); i++)
    {
      if (teamMembers.get(i).getId() == id)
      {
        return teamMembers.get(i);
      }
    }
    return null;
  }

  public TeamMember get(int index)
  {
    return teamMembers.get(index);
  }

  public int getSize()
  {
    return teamMembers.size();
  }

  public String toString()
  {
    return  "teamMembers=" + teamMembers;
  }
}
