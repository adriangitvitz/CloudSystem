import os
import yaml


class Environment:
    def __init__(self, config_name: str = "config.yml") -> None:
        self.config_name = config_name

    def load_environment(self):
        self.__load_yaml()

    def __load_yaml(self):
        config_file = self.__find_file()
        with open(config_file, "r") as f:
            yml_data = yaml.safe_load(f)
            self.__set_env_variables(yml_data)

    def __find_file(self) -> str:
        """
        Find if config file exists in root path.

        Returns:
        --------
        str
            Root path for the config yml file
        """
        CURRENT_PATH = os.path.dirname(os.path.abspath(__file__))
        config_path = os.path.join(CURRENT_PATH, self.config_name)
        if not os.path.exists(config_path):
            raise FileNotFoundError("An environment yml file should exists")
        return config_path

    def __set_env_variables(self, yml_data: dict):
        """
        Set env variables based on the yml file.

        This function gets the data from the YAML file in 'dict' form,
        based on that, first it gets the environment value from the 'env' field
        to get all the corresponding keys and values for that environment.

        Parameters:
        -----------
        yml_data : dict
                   Data from the YAML file.
        """
        environment_name = yml_data["env"]
        env_data = yml_data[environment_name]
        for key in env_data.keys():
            if type(env_data[key]).__name__ == "dict":
                for inner_key in env_data[key]:
                    keyname = f"{key.upper()}_{inner_key.upper()}"
                    if keyname in os.environ:
                        self.__unset_env_variables(keyname)
                    os.environ[keyname] = str(env_data[key][inner_key])
            if (
                type(env_data[key]).__name__ == "str"
                or type(env_data[key]).__name__ == "bool"
            ):
                if key in os.environ:
                    self.__unset_env_variables(keyname=key)
                os.environ[key.upper()] = str(env_data[key])
        self.__set_logger(env_name=environment_name)

    def __unset_env_variables(self, keyname: str):
        """Unset environment variable if exists."""
        del os.environ[keyname]

    def __set_logger(self, env_name: str):
        """
        Logger for this service.

        Parameters:
        -----------
        env_name : str
                   Environment name from yml file.
        """
        print(f"Environment: {env_name}")
