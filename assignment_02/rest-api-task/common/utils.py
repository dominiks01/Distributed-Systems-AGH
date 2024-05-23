from urllib.parse import urlencode, urlunparse


def build_url(base, path, params):
    """Utility function to build a URL with query parameters."""
    return urlunparse(
        (
            "http",  # Scheme
            base,  # Network location
            path,  # Path
            "",  # Params (not used)
            urlencode(params),  # Query
            "",  # Fragment
        )
    )
