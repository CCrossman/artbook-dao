-- Wrapper function to expose the @> operator to JPA
CREATE OR REPLACE FUNCTION jsonb_match(data jsonb, patch text)
RETURNS boolean AS $$
BEGIN
    -- Cast the text input to jsonb and check containment
    RETURN data @> patch::jsonb;
END;
$$ LANGUAGE plpgsql IMMUTABLE;