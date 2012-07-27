package au.org.intersect.exsite9.property;

import org.eclipse.core.expressions.PropertyTester;

import au.org.intersect.exsite9.domain.ResearchFile;

public class FileExistsTester extends PropertyTester
{
    @Override
    public boolean test(Object receiver, String property, Object[] args, Object expectedValue)
    {
        return ((ResearchFile) receiver).getFile().exists();
    }

}
